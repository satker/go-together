import React, {useState} from 'react';
import ObjectGeoLocation from "../../../utils/components/ObjectGeoLocation";
import {Event} from "../../../types";
import PropTypes from "prop-types";
import Button from "reactstrap/es/Button";
import {onChange} from "../../../utils/utils";
import {DEFAULT_ROUTE} from "../../../utils/constants";

const Route = ({event, onChangeEvent}) => {
    const [currentCenter, setCurrentCenter] = useState({lat: 18.5204, lng: 73.8567});
    const [routeNumber, setRouteNumber] = useState(event.route.length || 1);

    const onChangeLocation = (routeNumber, path, value) => {
        const newArray = [...event.route].map(rout => {
            if (rout.routeNumber === routeNumber) {
                onChange(rout, newRout => rout = newRout)(path, value);
                return rout;
            }
            return rout;
        });
        onChangeEvent('route', newArray);
    };

    const addLocation = () => {
        const newElement = {...DEFAULT_ROUTE};
        newElement.latitude = currentCenter.lat;
        newElement.longitude = currentCenter.lng;
        newElement.routeNumber = routeNumber;
        setRouteNumber(routeNumber + 1);
        onChangeEvent('route', [...event.route, newElement])
    };

    return <>
        Add {routeNumber} route point: {/*<ObjectGeoLocation
        isViewedAddress={true}
        onChange={onChange(currentRoute, setCurrentRoute)}
        draggable={true}
        longitude={currentRoute.longitude}
        latitude={currentRoute.latitude}
        header={'V'}
        height={400}
    />*/}
        <Button onClick={addLocation}>Add location</Button>
        <ObjectGeoLocation
            isViewedAddress={false}
            draggable={true}
            routes={event.route}
            onChange={onChangeLocation}
            height={400}
            setCurrentCenter={setCurrentCenter}
        /></>;
};

Route.propTypes = {
    event: Event.isRequired,
    onChangeEvent: PropTypes.func.isRequired
};

export default Route;