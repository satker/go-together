import React, {useCallback, useState} from 'react';
import ObjectGeoLocation from "../../../utils/components/ObjectGeoLocation";
import PropTypes from "prop-types";
import {onChange} from "../../../utils/utils";
import {DEFAULT_COUNTRY, DEFAULT_LOCATION, DEFAULT_ROUTE} from "../../../utils/constants";
import {updateEvent} from "../actions";
import {connect} from "../../../../App/Context";

const Route = ({eventRoute, updateEvent}) => {
    const [routeNumber, setRouteNumber] = useState(eventRoute.length || 1);

    const onChangeLocation = useCallback((updatedRouteNumber, path, value) => {
        const newArray = [...eventRoute].map(route => {
            if (route.routeNumber === updatedRouteNumber) {
                onChange(route, newRoute => route = newRoute)(path, value);
                return route;
            }
            return route;
        });
        updateEvent('route', newArray);
    }, [eventRoute, updateEvent]);

    const onDelete = (deletedRouteNumber) => {
        const newArray = [...eventRoute]
            .filter(route => route.routeNumber !== deletedRouteNumber)
            .map(route => {
                if (route.routeNumber > deletedRouteNumber) {
                    route.routeNumber = route.routeNumber - 1;
                }
                return route;
            });
        updateEvent('route', newArray);
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
        updateEvent('route', [...eventRoute, newElement])
    };

    return <>
        Add {routeNumber} route point (left click to add location):
        <ObjectGeoLocation
            onAdd={addLocation}
            onDelete={onDelete}
            editable={true}
            routes={eventRoute}
            onChange={onChangeLocation}
            height={400}
        />

    </>;
};

Route.propTypes = {
    eventRoute: PropTypes.array,
    updateEvent: PropTypes.func.isRequired
};

const mapStateToProps = () => (state) => ({
    eventRoute: state.components.forms.event.eventEdit.event.response.route
});

export default connect(mapStateToProps, {updateEvent})(Route);