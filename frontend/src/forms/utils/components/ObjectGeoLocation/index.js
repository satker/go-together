import React, {useCallback, useEffect, useState} from "react";
import PropTypes from "prop-types";
import GoogleMapReact from 'google-map-react';

import AutocompleteLocation from "forms/utils/components/AutocompleteLocation";
import ContainerColumn from "forms/utils/components/Container/ContainerColumn";
import LeftContainer from "forms/utils/components/Container/LeftContainer";
import RightContainer from "forms/utils/components/Container/RightContainer";
import Container from "forms/utils/components/Container/ContainerRow";
import ItemContainer from "forms/utils/components/Container/ItemContainer";

import Marker from "./Marker";
import {getAddress, getCity, getCountry, getState, GOOGLE_API_KEY, requestLatLng} from "./utils";
import RoutesList from "./RoutesList";

const getMapOptions = {
    disableDefaultUI: true,
    mapTypeControl: true,
    streetViewControl: true,
    styles: [{featureType: 'poi', elementType: 'labels', stylers: [{visibility: 'on'}]}],
};

const ObjectGeoLocation = ({routes, editable, onChange, zoom, onDelete, onAdd, height}) => {
    const [center, setCenter] = useState({lat: 18.5204, lng: 73.8567});
    const [isDraggable, setIsDraggable] = useState(true);
    const [zoomValue, setZoomValue] = useState(zoom || 9);
    const [currentKey, setCurrentKey] = useState(0);
    const [currentLat, setCurrentLat] = useState(center.lat);
    const [currentLng, setCurrentLng] = useState(center.lng);
    const [googleMap, setGoogleMap] = useState(null);
    const [polyline, setPolyline] = useState(null);

    useEffect(() => {
        const getCurrentRoute = routes.filter(route => route.routeNumber === currentKey)[0];
        if (routes.length !== 0 &&
            currentKey &&
            getCurrentRoute &&
            getCurrentRoute.latitude && getCurrentRoute.longitude &&
            (getCurrentRoute.latitude !== currentLat && currentLat !== 18.5204) &&
            (getCurrentRoute.longitude !== currentLng && currentLng !== 73.8567) &&
            editable) {
            onChange(currentKey, ['latitude', 'longitude'], [currentLat, currentLng]);
        }
    }, [routes, currentKey, editable, onChange, currentLat, currentLng]);

    const onCircleInteraction = (childKey, childProps, mouse) => {
        // function is just a stub to test callbacks
        setIsDraggable(false);
        setCurrentKey(parseInt(childKey));
        setCurrentLat(mouse.lat);
        setCurrentLng(mouse.lng);
    };

    const onCircleInteraction3 = (childKey, childProps, mouse) => {
        setIsDraggable(true);
        setCurrentKey(parseInt(childKey));
        setCurrentLat(mouse.lat);
        setCurrentLng(mouse.lng);
    };

    const _onChange = ({center, zoom}) => {
        setCenter(center);
        setZoomValue(zoom);
    };

    const endDrag = () => {
        requestLatLng(currentLat, currentLng, (result) => {
            const addressObject = result.results[0];
            onChange(currentKey, ['location.name', 'location.country.name', 'location.state', 'address'],
                [getCity(addressObject), getCountry(addressObject), getState(addressObject), getAddress(addressObject)]);
        });
    };

    const handleGoogleMapApi = (google) => {
        if (routes.length !== 0) {
            // center map to route
            const bounds = new google.maps.LatLngBounds();
            routes.map(route => ({lat: route.latitude, lng: route.longitude}))
                .forEach(route => bounds.extend(route));
            google.map.fitBounds(bounds);
        }
        if (google) {
            setGoogleMap(google);
        }
    };

    const getSortedRoutes = useCallback(() => routes
        .sort((route1, route2) => route1.routeNumber > route2.routeNumber ? 1 : -1), [routes]);

    useEffect(() => {
        if (googleMap) {
            let newPolyline = polyline;
            const newRoutes = getSortedRoutes().map(route => ({lat: route.latitude, lng: route.longitude}));
            if (newPolyline) {
                newPolyline.setMap(null);
                newPolyline.setPath(newRoutes);
                newPolyline.setMap(googleMap.map);
            } else {
                newPolyline = new googleMap.maps.Polyline({
                    path: [],
                    geodesic: true,
                    strokeColor: '#33BD4E',
                    strokeOpacity: 1,
                    strokeWeight: 5
                });
                newPolyline.setMap(googleMap.map);
                setPolyline(newPolyline);
            }
        }
    }, [getSortedRoutes, googleMap, polyline]);

    const getRoutes = () => getSortedRoutes()
        .map(route => <Marker
            key={route.routeNumber}
            lat={route.latitude}
            lng={route.longitude}
            name={route.address}
            color={getColorByRouteNumber(route.routeNumber)}
        />);

    const getColorByRouteNumber = (routeNumber) => {
        if (routeNumber === 1) {
            return 'green';
        } else if (routeNumber === routes.length) {
            return 'orange'
        } else {
            return 'red';
        }
    };

    const onAddMarker = ({lat, lng}) => {
        requestLatLng(lat, lng, (result) => {
            const addressObject = result.results[0];
            onAdd(['latitude', 'longitude', 'location.name',
                    'location.country.name', 'location.state', 'address'],
                [lat, lng, getCity(addressObject), getCountry(addressObject), getState(addressObject), getAddress(addressObject)]);
        });
    };

    return <Container>
        <ItemContainer>
            {editable && googleMap && <AutocompleteLocation setCenter={setCenter}/>}
        </ItemContainer>
        <ContainerColumn>
            <LeftContainer style={{width: '69%', height}}>
                <GoogleMapReact bootstrapURLKeys={{key: GOOGLE_API_KEY}}
                                yesIWantToUseGoogleMapApiInternals
                                onGoogleApiLoaded={handleGoogleMapApi}
                                draggable={isDraggable}
                                onChange={_onChange}
                                center={center}
                                zoom={zoomValue}
                                options={getMapOptions}
                                onChildMouseDown={editable ? onCircleInteraction : () => null}
                                onChildMouseUp={editable ? onCircleInteraction3 : () => null}
                                onChildMouseMove={editable ? onCircleInteraction : () => null}
                                onChildClick={editable ? endDrag : () => null}
                                onClick={editable ? onAddMarker : () => null}
                >
                    {getRoutes()}
                </GoogleMapReact>
            </LeftContainer>
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
    routes: PropTypes.array,
    editable: PropTypes.bool.isRequired,
    onChange: PropTypes.func,
    zoom: PropTypes.number,
    onDelete: PropTypes.func,
    onAdd: PropTypes.func,
    height: PropTypes.number
};

ObjectGeoLocation.defaultProps = {
    height: 400
};

export default ObjectGeoLocation;