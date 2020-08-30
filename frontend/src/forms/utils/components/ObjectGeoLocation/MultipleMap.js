import React, {useEffect, useState} from "react";

import Container from "forms/utils/components/Container/ContainerRow";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import AutocompleteLocation from "forms/utils/components/AutocompleteLocation";
import ContainerColumn from "forms/utils/components/Container/ContainerColumn";

import MapContainer from "./Common/MapContainer";
import EventsList from "./EventsList";
import ListContainer from "./Common/ListContainer";
import {getMarker} from "./utils";
import {usePosition} from "./hooks/usePosition";

const MultipleMap = ({routes, editable, onChange, zoom, onDelete, onAdd, height}) => {
    const [center, setCenter] = useState(usePosition());
    const [googleMap, setGoogleMap] = useState(null);
    const [polyline, setPolyline] = useState([]);
    const [selectedEvent, setSelectedEvent] = useState(null);

    useEffect(() => {
        if (googleMap) {
            const setMultiplePolyLine = (route) => {
                let newPolylines = new googleMap.maps.Polyline({
                    path: route.locations.map(route => ({lat: route.latitude, lng: route.longitude})),
                    geodesic: true,
                    strokeColor: selectedEvent !== route.id ? '#33BD4E' : '#0b4a16',
                    strokeOpacity: 1,
                    strokeWeight: 5
                });
                newPolylines.setMap(googleMap.map);
                setPolyline([...polyline, newPolylines]);
            }

            routes.map(route => setMultiplePolyLine(route));
        }
    }, [routes, googleMap, selectedEvent]);

    const sort = (routes) =>
        routes.locations =
            routes.locations.sort((route1, route2) => route1.routeNumber > route2.routeNumber ? 1 : -1);

    const getSortedRoutes = () => {
        return routes.map(multipleRoutes => sort(multipleRoutes));
    };

    const getRoutes = () => getSortedRoutes().map(route => getMarker(route));

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
                <EventsList routes={routes}
                            googleMap={googleMap}
                            selectedEvent={selectedEvent}
                            setSelectedEvent={setSelectedEvent}
                />
            </ListContainer>
        </ContainerColumn>
    </Container>;
}

export default MultipleMap;