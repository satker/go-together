import React, {useEffect, useState} from "react";
import PropTypes from "prop-types";
import GoogleMapReact from 'google-map-react';

import LeftContainer from "forms/utils/components/Container/LeftContainer";
import {getAddress, getCity, getCountry, getState, GOOGLE_API_KEY, requestLatLng} from '../utils';

const getMapOptions = {
    disableDefaultUI: true,
    mapTypeControl: true,
    streetViewControl: true,
    styles: [{featureType: 'poi', elementType: 'labels', stylers: [{visibility: 'on'}]}],
};

const MapContainer = ({
                          route, routes, editable, onChange, zoom,
                          onAdd, height, children, setGoogleMap,
                          center, setCenter
                      }) => {
    const [isDraggable, setIsDraggable] = useState(true);
    const [zoomValue, setZoomValue] = useState(zoom || 9);
    const [currentKey, setCurrentKey] = useState(0);
    const [currentLat, setCurrentLat] = useState(center.lat);
    const [currentLng, setCurrentLng] = useState(center.lng);

    useEffect(() => {
        if (editable && route) {
            const getCurrentRoute = route.filter(route => route.routeNumber === currentKey)[0];
            if (route.length !== 0 &&
                currentKey &&
                getCurrentRoute &&
                getCurrentRoute.latitude && getCurrentRoute.longitude &&
                (getCurrentRoute.latitude !== currentLat && currentLat !== 18.5204) &&
                (getCurrentRoute.longitude !== currentLng && currentLng !== 73.8567)) {
                onChange(currentKey, ['latitude', 'longitude'], [currentLat, currentLng]);
            }
        }
    }, [route, currentKey, editable, onChange, currentLat, currentLng]);

    const onCircleInteraction = (childKey, childProps, mouse) => {
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
            onChange(currentKey, ['place.name', 'place.country.name', 'place.state', 'address'],
                [getCity(addressObject), getCountry(addressObject), getState(addressObject), getAddress(addressObject)]);
        });
    };

    const extendBounds = (routes, bounds) => {
        routes.map(route => ({lat: route.latitude, lng: route.longitude}))
            .forEach(route => bounds.extend(route));
    }

    const handleGoogleMapApi = (google) => {
        const bounds = new google.maps.LatLngBounds();
        routes ?
            routes.map(multipleRoutes => extendBounds(multipleRoutes.locations, bounds)) :
            extendBounds(route, bounds);
        google.map.fitBounds(bounds);
        if (google) {
            setGoogleMap(google);
        }
    };

    const onAddMarker = ({lat, lng}) => {
        requestLatLng(lat, lng, (result) => {
            const addressObject = result.results[0];
            onAdd(['latitude', 'longitude', 'place.name',
                    'place.country.name', 'place.state', 'address'],
                [lat, lng, getCity(addressObject), getCountry(addressObject), getState(addressObject), getAddress(addressObject)]);
        });
    };

    return <LeftContainer style={{width: '69%', height}}>
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
            {children}
        </GoogleMapReact>
    </LeftContainer>;
};

MapContainer.props = {
    route: PropTypes.array,
    routes: PropTypes.array,
    editable: PropTypes.bool.isRequired,
    onChange: PropTypes.func,
    zoom: PropTypes.number,
    onDelete: PropTypes.func,
    onAdd: PropTypes.func,
    height: PropTypes.number,
};

MapContainer.defaultProps = {
    height: 400
};

export default MapContainer;