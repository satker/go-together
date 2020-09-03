import React, {useEffect, useState} from "react";
import PropTypes from "prop-types";

import AutocompleteLocation from "forms/utils/components/AutocompleteLocation";
import ContainerColumn from "forms/utils/components/Container/ContainerColumn";
import Container from "forms/utils/components/Container/ContainerRow";
import ItemContainer from "forms/utils/components/Container/ItemContainer";

import RoutesList from './RoutesList';
import MapContainer from "./Common/MapContainer";
import ListContainer from "./Common/ListContainer";
import {getMarker, sort} from "./utils";
import {usePosition} from "./hooks/usePosition";

const SingleMap = ({route, editable, onChange, zoom, onDelete, onAdd, height}) => {
    const [center, setCenter] = useState(usePosition());
    const [googleMap, setGoogleMap] = useState(null);
    const [polyline, setPolyline] = useState(null);
    const [selected, setSelected] = useState(null);

    useEffect(() => {
        if (googleMap) {
            let newPolyline = polyline;
            const newRoutes = route.map(route => ({lat: route.latitude, lng: route.longitude}));
            if (newPolyline) {
                newPolyline.setMap(null);
                newPolyline.setPath(newRoutes);
                newPolyline.setMap(googleMap.map);
            } else {
                newPolyline = new googleMap.maps.Polyline({
                    path: newRoutes,
                    geodesic: true,
                    strokeColor: '#33BD4E',
                    strokeOpacity: 1,
                    strokeWeight: 3
                });
                newPolyline.setMap(googleMap.map);
                setPolyline(newPolyline);
            }
        }
    }, [googleMap, route]);

    const getRoutes = () => getMarker(sort(route), editable || ((route) => centerPlace(route)()));

    const centerPlace = (route) => () => {
        setCenter({lat: route.latitude, lng: route.longitude})
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
                          route={route}
                          zoom={zoom}
                          setGoogleMap={setGoogleMap}
                          center={center}
                          setCenter={setCenter}
            >
                {getRoutes()}
            </MapContainer>
            <ListContainer height={height}>
                <RoutesList selected={selected}
                            centerPlace={centerPlace}
                            onDelete={onDelete}
                            routes={sort(route)}
                            editable={editable}/>
            </ListContainer>
        </ContainerColumn>
    </Container>;
};

SingleMap.props = {
    route: PropTypes.array,
    routes: PropTypes.array,
    editable: PropTypes.bool.isRequired,
    onChange: PropTypes.func,
    zoom: PropTypes.number,
    onDelete: PropTypes.func,
    onAdd: PropTypes.func,
    height: PropTypes.number,
    multipleRoutes: PropTypes.bool
};

SingleMap.defaultProps = {
    height: 400
};

export default SingleMap;