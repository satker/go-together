import React, {useEffect, useState} from "react";
import PropTypes from "prop-types";

import AutocompleteLocation from "forms/utils/components/AutocompleteLocation";
import ContainerColumn from "forms/utils/components/Container/ContainerColumn";
import RightContainer from "forms/utils/components/Container/RightContainer";
import Container from "forms/utils/components/Container/ContainerRow";
import ItemContainer from "forms/utils/components/Container/ItemContainer";

import Marker from './Marker';
import RoutesList from './RoutesList';
import ObjectGeoLocationContainer from "./ObjectGeoLocationContainer";

const ObjectGeoLocation = ({route, editable, onChange, zoom, onDelete, onAdd, height}) => {
    const [center, setCenter] = useState({lat: 18.5204, lng: 73.8567});
    const [googleMap, setGoogleMap] = useState(null);
    const [polyline, setPolyline] = useState(null);

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
                    strokeWeight: 5
                });
                newPolyline.setMap(googleMap.map);
                setPolyline(newPolyline);
            }
        }
    }, [googleMap, route]);

    const getSortedRoutes = () => route.sort((route1, route2) => route1.routeNumber > route2.routeNumber ? 1 : -1);

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
    const getRoutes = () => getMarker(getSortedRoutes());

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
                                        route={route}
                                        zoom={zoom}
                                        setGoogleMap={setGoogleMap}
                                        center={center}
                                        setCenter={setCenter}
            >
                {getRoutes()}
            </ObjectGeoLocationContainer>
            <RightContainer isBordered={true} style={{width: '30%', height}}>
                <RoutesList setCenter={setCenter}
                            onDelete={onDelete}
                            center={center}
                            routes={getSortedRoutes()}
                            editable={editable}/>
            </RightContainer>
        </ContainerColumn>
    </Container>;
};

ObjectGeoLocation.props = {
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

ObjectGeoLocation.defaultProps = {
    height: 400
};

export default ObjectGeoLocation;