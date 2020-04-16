import React, {useEffect, useState} from "react";
import PropTypes from "prop-types";
import GoogleMapReact from 'google-map-react';

import "./style_place.css";
import {fetchAndSet} from "../../api/request";
import Marker from "./Marker";
import {getAddress, getCity, getCountry, getState} from "./utils";
import RouteItem from "./RouteItem";
import {ListGroup} from "reactstrap";
import AddressFields from "./AddressFields";
import ContainerColumn from "../Container/ContainerColumn";
import LeftContainer from "../Container/LeftContainer";
import RightContainer from "../Container/RightContainer";

export const GOOGLE_API_KEY = "AIzaSyBSjnMkN8ckymUWZO5v0q-cZW9WppoFsyM";
const getMapOptions = () => {
    return {
        disableDefaultUI: true,
        mapTypeControl: true,
        streetViewControl: true,
        styles: [{featureType: 'poi', elementType: 'labels', stylers: [{visibility: 'on'}]}],
    };
};

const ObjectGeoLocation = ({routes, editable, onChange, zoom, onDelete, onAdd}) => {
    const [center, setCenter] = useState([18.5204, 73.8567]);
    const [isDraggable, setIsDraggable] = useState(true);
    const [zoomValue, setZoomValue] = useState(zoom || 9);
    const [currentKey, setCurrentKey] = useState(0);
    const [currentLat, setCurrentLat] = useState(center[0]);
    const [currentLng, setCurrentLng] = useState(center[1]);
    const [googleMap, setGoogleMap] = useState(null);
    const [polyline, setPolyline] = useState(null);
    const [lock, setLock] = useState(false);

    const URL_FROM_LAN_LNG_TO_LOCATION = `https://maps.googleapis.com/maps/api/geocode/json?latlng=${currentLat},${currentLng}
    &key=${GOOGLE_API_KEY}&language=en`;


    useEffect(() => {
        routes.filter(route => !route.address)
            .forEach(route => {
                const URL_FROM_LAN_LNG_TO_LOCATION_CUSTOM = `https://maps.googleapis.com/maps/api/geocode/json?latlng=${route.latitude},
                ${route.longitude}&key=${GOOGLE_API_KEY}&language=en`;
                fetchAndSet(URL_FROM_LAN_LNG_TO_LOCATION_CUSTOM, (result) => {
                    setLock(false);
                    onChange(route.routeNumber, ['location.name', 'location.country.name', 'location.state', 'address'],
                        [getCity(result), getCountry(result), getState(result), getAddress(result)]);
                })
            })
    }, [routes, onChange]);
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
    }, [routes, currentKey, editable, URL_FROM_LAN_LNG_TO_LOCATION, onChange, currentLat, currentLng]);

    const onCircleInteraction = (childKey, childProps, mouse) => {
        // function is just a stub to test callbacks
        setIsDraggable(false);
        setCurrentKey(parseInt(childKey));
        setCurrentLat(mouse.lat);
        setCurrentLng(mouse.lng);
        setLock(false)
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
        setLock(false)
    };

    const endDrag = () => {
        fetchAndSet(URL_FROM_LAN_LNG_TO_LOCATION, (result) => {
            setLock(false);
            onChange(currentKey, ['location.name', 'location.country.name', 'location.state', 'address'],
                [getCity(result), getCountry(result), getState(result), getAddress(result)]);
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
            handlePolyline(google);
        }
    };

    const handlePolyline = (google) => {
        let newPolyline = polyline;
        const newRoutes = getSortedRoutes().map(route => ({lat: route.latitude, lng: route.longitude}));
        if (newPolyline) {
            newPolyline.setMap(null);
            newPolyline.setPath(newRoutes);
            newPolyline.setMap(google.map);
        } else {
            newPolyline = new google.maps.Polyline({
                path: [],
                geodesic: true,
                strokeColor: '#33BD4E',
                strokeOpacity: 1,
                strokeWeight: 5
            });
            newPolyline.setMap(google.map);
            setPolyline(newPolyline);
        }
        setLock(true)
    };

    const getSortedRoutes = () => routes
        .sort((route1, route2) => route1.routeNumber > route2.routeNumber ? 1 : -1);

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

    return <>
        {editable && googleMap && <AddressFields google={googleMap} setCenter={setCenter}/>}
        <ContainerColumn>
            <LeftContainer style={{width: '70%', height: 400}}>
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
                                onClick={editable ? ((evt) => onAdd(evt.lat, evt.lng)) : () => null}
                >
                    {googleMap && !lock && handlePolyline(googleMap)}
                    {getRoutes()}
                </GoogleMapReact>
            </LeftContainer>
            <RightContainer isBordered={true} style={{width: '30%'}}><ListGroup>
                {getSortedRoutes().map(route => <RouteItem key={route.routeNumber}
                                                           onDelete={editable && ((routeNumber) => {
                                                               onDelete(routeNumber);
                                                               setLock(false);
                                                           })}
                                                           route={route}
                                                           center={center}
                                                           setCenter={setCenter}/>)}
            </ListGroup>
            </RightContainer>
        </ContainerColumn>
    </>;
};

ObjectGeoLocation.props = {
    routes: PropTypes.array,
    editable: PropTypes.bool.isRequired,
    onChange: PropTypes.func,
    zoom: PropTypes.number,
    onDelete: PropTypes.func,
    onAdd: PropTypes.func
};

export default ObjectGeoLocation;