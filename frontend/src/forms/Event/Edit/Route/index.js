import React, {useState} from 'react';
import ObjectGeoLocation from "../../../utils/components/ObjectGeoLocation";
import {Event} from "../../../utils/types";
import PropTypes from "prop-types";
import {onChange} from "../../../utils/utils";
import {DEFAULT_COUNTRY, DEFAULT_LOCATION, DEFAULT_ROUTE} from "../../../utils/constants";

const Route = ({event, onChangeEvent}) => {
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

    const addLocation = (lat, lng) => {
        const newElement = {...DEFAULT_ROUTE};
        newElement.latitude = lat;
        newElement.longitude = lng;
        newElement.routeNumber = routeNumber;
        newElement.location = {...DEFAULT_LOCATION};
        newElement.location.country = {...DEFAULT_COUNTRY};
        setRouteNumber(routeNumber + 1);
        onChangeEvent('route', [...event.route, newElement])
    };

    return <>
        Add {routeNumber} route point (left click to add location):
        <ObjectGeoLocation
            onAdd={addLocation}
            onDelete={onDelete}
            editable={true}
            routes={event.route}
            onChange={onChangeLocation}
            height={400}
        />

    </>;
};

Route.propTypes = {
    event: Event.isRequired,
    onChangeEvent: PropTypes.func.isRequired
};

export default Route;