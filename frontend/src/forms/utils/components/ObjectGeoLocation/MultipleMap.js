import React, {useEffect, useState} from "react";

import Container from "forms/utils/components/Container/ContainerRow";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import AutocompleteLocation from "forms/utils/components/AutocompleteLocation";
import ContainerColumn from "forms/utils/components/Container/ContainerColumn";

import MapContainer from "./Common/MapContainer";
import EventsList from "./EventsList";
import ListContainer from "./Common/ListContainer";
import {getMarker, sort} from "./utils";
import {usePosition} from "./hooks/usePosition";

const MultipleMap = ({routes, editable, onChange, zoom, onDelete, onAdd, height}) => {
    const [center, setCenter] = useState(usePosition());
    const [googleMap, setGoogleMap] = useState(null);
    const [polyline, setPolyline] = useState([]);
    const [selectedEvent, setSelectedEvent] = useState(null);

    useEffect(() => {
        if (googleMap) {
            const setMultiplePolyLine = (route) => {
                const routePath = route.locations.map(route => ({lat: route.latitude, lng: route.longitude}));
                let newPolyline = new googleMap.maps.Polyline({
                    path: routePath,
                    geodesic: true,
                    strokeColor: selectedEvent !== route.id ? '#33BD4E' : '#0b4a16',
                    strokeOpacity: 1,
                    strokeWeight: 3
                });
                newPolyline.setMap(googleMap.map);
                setPolyline([...polyline, newPolyline]);
                googleMap.maps.event.addListener(newPolyline, 'click', () => setSelectedEvent(route.id));
            }

            routes.map(route => setMultiplePolyLine(route));
        }
    }, [routes, googleMap, selectedEvent]);

    const getRoutes = () => routes.map(route => {
        const sortedLocations = sort(route.locations);
        return getMarker(sortedLocations, () => setSelectedEvent(route.id))
    });

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