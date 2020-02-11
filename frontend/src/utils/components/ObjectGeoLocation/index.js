import React, {useEffect, useState} from "react";
import PropTypes from "prop-types";
import GoogleMapReact from 'google-map-react';

import "./style_place.css";
import AddressFields from "./AddressFields";
import {fetchAndSet} from "../../api/request";

const GOOGLE_API_KEY = "AIzaSyBSjnMkN8ckymUWZO5v0q-cZW9WppoFsyM";

const ObjectGeoLocation = ({header, latitude, longitude, draggable, onChange, zoom}) => {
    const [center, setCenter] = useState([latitude, longitude]);
    const [isDraggable, setIsDraggable] = useState(true);
    const [zoomValue, setZoomValue] = useState(zoom || 9);
    const [lat, setLat] = useState(latitude);
    const [lng, setLng] = useState(longitude);
    const [response, setResponse] = useState(null);

    const URL_FROM_LAN_LNG_TO_LOCATION = `https://maps.googleapis.com/maps/api/geocode/json?latlng=${lat},${lng}
    &key=${GOOGLE_API_KEY}&language=en`;

    useEffect(() => {
        if (lat && lng && !response && draggable) {
            fetchAndSet(URL_FROM_LAN_LNG_TO_LOCATION, setResponse);
            onChange(lat, 'location.latitude');
            onChange(lng, 'location.longitude');
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
            onChange(lat, 'location.latitude');
            onChange(lng, 'location.longitude')
        });
    };

    return <div className='flex'>
        {response && response.results[0] && <AddressFields response={response}
                                                           onChange={onChange}/>}
        <div style={{width: '100%', height: 400}}>
            <GoogleMapReact bootstrapURLKeys={{key: GOOGLE_API_KEY}}
                            draggable={isDraggable}
                            onChange={_onChange}
                            center={center}
                            zoom={zoomValue}
                            onChildMouseDown={draggable ? onCircleInteraction : () => null}
                            onChildMouseUp={draggable ? onCircleInteraction3 : () => null}
                            onChildMouseMove={draggable ? onCircleInteraction : () => null}
                            onChildClick={draggable ? endDrag : () => null}
                            onClick={() => null}
            >
                <div
                    className="place"
                    lat={lat}
                    lng={lng}>
                    {header}
                </div>
            </GoogleMapReact>
        </div>
    </div>;
};

ObjectGeoLocation.props = {
    header: PropTypes.string.isRequired,
    latitude: PropTypes.number.isRequired,
    longitude: PropTypes.number.isRequired,
    draggable: PropTypes.bool.isRequired,
    onChange: PropTypes.func,
    zoom: PropTypes.number
};

export default ObjectGeoLocation;