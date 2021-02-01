import React, {useEffect, useState} from "react";

import Container from "forms/utils/components/Container/ContainerRow";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import AutocompleteLocation from "forms/utils/components/AutocompleteLocation";
import ContainerColumn from "forms/utils/components/Container/ContainerColumn";

import MapContainer from "./Common/MapContainer";
import ListContainer from "./Common/ListContainer";
import {getMarker, sort} from "./utils";
import {usePosition} from "./Position/usePosition";
import PropTypes from "prop-types";
import {EventMapRoute} from "forms/utils/types";

const MultipleMap = ({routes, editable, onChange, zoom, onDelete, onAdd, height, children}) => {
    const [center, setCenter] = useState(usePosition());
    const [googleMap, setGoogleMap] = useState(null);
    const [polyline, setPolyline] = useState([]);
    const [selected, setSelected] = useState(null);

    useEffect(() => {
        if (googleMap) {
            const setMultiplePolyLine = (route) => {
                const routePath = route.locations.map(route => ({
                    lat: route.location.latitude,
                    lng: route.location.longitude
                }));
                let newPolyline = new googleMap.maps.Polyline({
                    path: routePath,
                    geodesic: true,
                    strokeColor: selected !== route.id ? '#33BD4E' : '#0b4a16',
                    strokeOpacity: 1,
                    strokeWeight: 3
                });
                newPolyline.setMap(googleMap.map);
                setPolyline([...polyline, newPolyline]);
                googleMap.maps.event.addListener(newPolyline, 'click', () => setSelected(route.id));
            }

            routes.map(route => setMultiplePolyLine(route));
        }
    }, [routes, googleMap, selected]);

    const getRoutes = () => routes.map(route => getMarker(sort(route.locations), () => setSelected(route.id)));

    const centerLocations = (route) => () => {
        const bounds = new googleMap.maps.LatLngBounds();
        route.locations.map(route => ({lat: route.location.latitude, lng: route.location.longitude}))
            .forEach(route => bounds.extend(route));
        googleMap.map.fitBounds(bounds);
        setSelected(route.id);
    };

    return <Container>
        <ItemContainer>
            {editable && googleMap && <AutocompleteLocation setCenter={setCenter}/>}
        </ItemContainer>
        <ContainerColumn>
            <MapContainer editable={editable}
                          height={height}
                          onAdd={onAdd}
                          onChange={onChange}
                          onDelete={onDelete}
                          routes={routes}
                          zoom={zoom}
                          setGoogleMap={setGoogleMap}
                          center={center}
                          setCenter={setCenter}
            >
                {getRoutes()}
            </MapContainer>
            <ListContainer height={height}>
                {React.Children.map(children, child =>
                    React.cloneElement(child, {
                        ...child.props,
                        routes,
                        selected,
                        centerLocations
                    })
                )}
            </ListContainer>
        </ContainerColumn>
    </Container>;
}

MultipleMap.propTypes = {
    routes: PropTypes.arrayOf(EventMapRoute),
    editable: PropTypes.bool,
    onChange: PropTypes.func,
    zoom: PropTypes.number,
    onDelete: PropTypes.func,
    onAdd: PropTypes.func,
    height: PropTypes.number,
}

export default MultipleMap;