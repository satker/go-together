import React, {useEffect, useState} from "react";
import PropTypes from "prop-types";
import GoogleMapReact from 'google-map-react';

import "./style_place.css";
import AddressFields from "./AddressFields";
import {fetchAndSet} from "../../api/request";
import Marker from "./Marker";

const GOOGLE_API_KEY = "AIzaSyBSjnMkN8ckymUWZO5v0q-cZW9WppoFsyM";
const getMapOptions = () => {
    return {
        disableDefaultUI: true,
        mapTypeControl: true,
        streetViewControl: true,
        styles: [{featureType: 'poi', elementType: 'labels', stylers: [{visibility: 'on'}]}],
    };
};

const ObjectGeoLocation = ({isViewedAddress, routes, draggable, onChange, zoom, setCurrentCenter}) => {
    const [center, setCenter] = useState([18.5204, 73.8567]);
    const [isDraggable, setIsDraggable] = useState(true);
    const [zoomValue, setZoomValue] = useState(zoom || 9);
    const [currentKey, setCurrentKey] = useState(0);
    const [currentLat, setCurrentLat] = useState(center[0]);
    const [currentLng, setCurrentLng] = useState(center[1]);
    const [response, setResponse] = useState(null);

    const URL_FROM_LAN_LNG_TO_LOCATION = `https://maps.googleapis.com/maps/api/geocode/json?latlng=${currentLat},${currentLng}
    &key=${GOOGLE_API_KEY}&language=en`;

    const getCurrentRoute = routes.filter(route => route.routeNumber === currentKey)[0];

    useEffect(() => {
        if (routes.length !== 0 && currentKey && getCurrentRoute.latitude && getCurrentRoute.longitude &&
            (getCurrentRoute.latitude !== currentLat && currentLat !== 18.5204) &&
            (getCurrentRoute.longitude !== currentLng && currentLng !== 73.8567)
            && /*!response && */draggable) {
            //fetchAndSet(URL_FROM_LAN_LNG_TO_LOCATION, setResponse);
            onChange(currentKey, ['latitude', 'longitude'], [currentLat, currentLng]);
        }
    }, [routes, currentKey, response, draggable, URL_FROM_LAN_LNG_TO_LOCATION, onChange, currentLat, currentLng]);

    const onCircleInteraction = (childKey, childProps, mouse) => {
        // function is just a stub to test callbacks
        setIsDraggable(false);
        setCurrentKey(parseInt(childKey));
        setCurrentLat(mouse.lat);
        setCurrentLng(mouse.lng);
    };

    const onCircleInteraction3 = (childKey, childProps, mouse) => {
        setIsDraggable(true);
    };

    const _onChange = ({center, zoom}) => {
        setCenter(center);
        setCurrentCenter(center);
        setZoomValue(zoom);
    };

    const endDrag = () => {
        fetchAndSet(URL_FROM_LAN_LNG_TO_LOCATION, (result) => {
            setResponse(result);
            //onChange(currentKey, ['latitude', 'longitude'], [currentLat, currentLng]);
        });
    };

    return <div className='flex'>
        {isViewedAddress && response && response.results[0] && <AddressFields response={response}
                                                                              onChange={onChange}/>}
        <div style={{width: '100%', height: 400}}>
            <GoogleMapReact bootstrapURLKeys={{key: GOOGLE_API_KEY}}
                            draggable={isDraggable}
                            onChange={_onChange}
                            center={center}
                            zoom={zoomValue}
                            options={getMapOptions}
                            onChildMouseDown={draggable ? onCircleInteraction : () => null}
                            onChildMouseUp={draggable ? onCircleInteraction3 : () => null}
                            onChildMouseMove={draggable ? onCircleInteraction : () => null}
                            onChildClick={draggable ? endDrag : () => null}
                            onClick={() => null}
            >
                {routes.map(route => <Marker
                    key={route.routeNumber}
                    lat={route.latitude}
                    lng={route.longitude}
                    name={route.routeNumber}
                    color="red"
                />)}
            </GoogleMapReact>
        </div>
    </div>;
};

ObjectGeoLocation.props = {
    isViewedAddress: PropTypes.bool.isRequired,
    setCurrentCenter: PropTypes.func,
    routes: PropTypes.array,
    draggable: PropTypes.bool.isRequired,
    onChange: PropTypes.func,
    zoom: PropTypes.number
};

export default ObjectGeoLocation;