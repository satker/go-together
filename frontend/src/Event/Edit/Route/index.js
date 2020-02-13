import React, {useState} from 'react';
import ObjectGeoLocation from "../../../utils/components/ObjectGeoLocation";
import {Event} from "../../../types";
import PropTypes from "prop-types";
import Button from "reactstrap/es/Button";
import {DEFAULT_ROUTE} from "../../../utils/constants";
import {onChange} from "../../../utils/utils";

const Route = ({event, onChangeEvent}) => {
    const [currentRoute, setCurrentRoute] = useState({...DEFAULT_ROUTE});
    const [routeNumber, setRouteNumber] = useState(event.route.length || 1);

    const addLocation = () => {
        const newElement = {...currentRoute};
        newElement.routeNumber = routeNumber;
        setRouteNumber(routeNumber + 1);
        console.log('newElement', newElement)
        onChangeEvent('route', [...event.route, newElement])
    };

    return <>
        Add {routeNumber} route point: <ObjectGeoLocation
        isViewedAddress={true}
        onChange={onChange(currentRoute, setCurrentRoute)}
        draggable={true}
        longitude={currentRoute.longitude}
        latitude={currentRoute.latitude}
        header={'V'}
        height={400}
    />
        <Button onClick={addLocation}>Add location</Button>
        <ObjectGeoLocation
            isViewedAddress={false}
            draggable={false}
            latlngPairs={event.route.map(r => ({latitude: r.latitude, longitude: r.longitude}))}
            header={'V'}
            height={400}
        /></>;
};

Route.propTypes = {
    event: Event.isRequired,
    onChangeEvent: PropTypes.func.isRequired
};

export default Route;