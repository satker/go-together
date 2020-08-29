import React, {useEffect, useState} from "react";
import Marker from "./Marker";
import Container from "../Container/ContainerRow";
import ItemContainer from "../Container/ItemContainer";
import AutocompleteLocation from "../AutocompleteLocation";
import ContainerColumn from "../Container/ContainerColumn";
import ObjectGeoLocationContainer from "./ObjectGeoLocationContainer";
import RightContainer from "../Container/RightContainer";
import EventsList from "./EventsList";

const MultipleMap = ({routes, editable, onChange, zoom, onDelete, onAdd, height}) => {
    const [center, setCenter] = useState({lat: 18.5204, lng: 73.8567});
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

    const getMarker = (routes) => {
        return routes
            .map(route => <Marker
                key={route.routeNumber}
                lat={route.latitude}
                lng={route.longitude}
                name={route.address}
                color={getColorByRouteNumber(route, routes)}
            />);
    }
    const getRoutes = () => getSortedRoutes().map(route => getMarker(route));

    const getColorByRouteNumber = (route, routes) => {
        if (route.routeNumber === 1) {
            return 'green';
        } else if (route.routeNumber === routes.length) {
            return 'red'
        } else {
            return 'orange';
        }
    };

    return <Container>
        <ItemContainer>
            {editable && googleMap && <AutocompleteLocation setCenter={setCenter}/>}
        </ItemContainer>
        <ContainerColumn>
            <ObjectGeoLocationContainer editable={editable}
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
            </ObjectGeoLocationContainer>
            <RightContainer isBordered={true} style={{width: '30%', height}}>
                <EventsList routes={routes}
                            googleMap={googleMap}
                            selected={selectedEvent}
                            setSelectedEvent={setSelectedEvent}
                />
            </RightContainer>
        </ContainerColumn>
    </Container>;
}

export default MultipleMap;