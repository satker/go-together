import React, {useState} from 'react';
import ObjectGeoLocation from "../../../utils/components/ObjectGeoLocation";
import {Event} from "../../../types";
import PropTypes from "prop-types";
import Button from "reactstrap/es/Button";
import {onChange} from "../../../utils/utils";
import {DEFAULT_COUNTRY, DEFAULT_LOCATION, DEFAULT_ROUTE} from "../../../utils/constants";

const Route = ({event, onChangeEvent}) => {
    const [currentCenter, setCurrentCenter] = useState({lat: 18.5204, lng: 73.8567});
    const [routeNumber, setRouteNumber] = useState(event.route.length || 1);

    const onChangeLocation = (updatedRouteNumber, path, value) => {
        const newArray = [...event.route].map(route => {
            if (route.routeNumber === updatedRouteNumber) {
                onChange(route, newRoute => route = newRoute)(path, value);
                return route;
            }
            return route;
        });
        onChangeEvent('route', newArray);
    };

    const onDelete = (deletedRouteNumber) => {
        const newArray = [...event.route]
            .filter(route => route.routeNumber !== deletedRouteNumber)
            .map(route => {
                if (route.routeNumber > deletedRouteNumber) {
                    route.routeNumber = route.routeNumber - 1;
                }
                return route;
            });
        onChangeEvent('route', newArray);
        setRouteNumber(routeNumber - 1);
    };

    const addLocation = () => {
        const newElement = {...DEFAULT_ROUTE};
        newElement.latitude = currentCenter.lat;
        newElement.longitude = currentCenter.lng;
        newElement.routeNumber = routeNumber;
        newElement.location = {...DEFAULT_LOCATION};
        newElement.location.country = {...DEFAULT_COUNTRY};
        setRouteNumber(routeNumber + 1);
        onChangeEvent('route', [...event.route, newElement])
    };

    return <>
        Add {routeNumber} route point:
        <Button onClick={addLocation}>Add location</Button>
        <ObjectGeoLocation
            onDelete={onDelete}
            isViewedAddress={false}
            draggable={true}
            routes={event.route}
            onChange={onChangeLocation}
            height={400}
            setCurrentCenter={setCurrentCenter}
        />

    </>;
};

Route.propTypes = {
    event: Event.isRequired,
    onChangeEvent: PropTypes.func.isRequired
};

export default Route;