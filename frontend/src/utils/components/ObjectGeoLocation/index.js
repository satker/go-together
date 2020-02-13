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

const ObjectGeoLocation = ({header, isViewedAddress, latitude, longitude, latlngPairs, draggable, onChange, zoom}) => {
    const [center, setCenter] = useState([latitude || 18.5204, longitude || 73.8567]);
    const [isDraggable, setIsDraggable] = useState(true);
    const [zoomValue, setZoomValue] = useState(zoom || 9);
    const [lat, setLat] = useState(latitude);
    const [lng, setLng] = useState(longitude);
    const [response, setResponse] = useState(null);

    const URL_FROM_LAN_LNG_TO_LOCATION = `https://maps.googleapis.com/maps/api/geocode/json?latlng=${lat},${lng}
    &key=${GOOGLE_API_KEY}&language=en`;

    useEffect(() => {
        if (lat && lng && lat !== latitude && lng !== longitude && !response && draggable) {
            console.log(lat, lng)
            fetchAndSet(URL_FROM_LAN_LNG_TO_LOCATION, setResponse);
            onChange(['latitude', 'longitude'], [lat, lng]);
        }
    }, [lat, lng, response, draggable, URL_FROM_LAN_LNG_TO_LOCATION, onChange]);

    const onCircleInteraction = (childKey, childProps, mouse) => {
        // function is just a stub to test callbacks
        setIsDraggable(false);
        setLat(mouse.lat);
        setLng(mouse.lng);
    };

    const onCircleInteraction3 = (childKey, childProps, mouse) => {
        setIsDraggable(true);
    };

    const _onChange = ({center, zoom}) => {
        setCenter(center);
        setZoomValue(zoom);
    };

    const endDrag = () => {
        fetchAndSet(URL_FROM_LAN_LNG_TO_LOCATION, (result) => {
            setResponse(result);
            onChange(['latitude', 'longitude'], [lat, lng]);
        });
    };

    const viewMarkers = isViewedAddress ? <Marker
        lat={lat}
        lng={lng}
        name={header}
        color="red"
    /> : latlngPairs.map(latlng => <Marker
        lat={latlng.latitude}
        lng={latlng.longitude}
        name={header}
        color="red"
    />);

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
                {viewMarkers}
            </GoogleMapReact>
        </div>
    </div>;
};

ObjectGeoLocation.props = {
    isViewedAddress: PropTypes.bool.isRequired,
    header: PropTypes.string.isRequired,
    latitude: PropTypes.number.isRequired,
    longitude: PropTypes.number.isRequired,
    latlngPairs: PropTypes.array,
    draggable: PropTypes.bool.isRequired,
    onChange: PropTypes.func,
    zoom: PropTypes.number
};

export default ObjectGeoLocation;