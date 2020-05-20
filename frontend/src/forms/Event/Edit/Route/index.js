import React, {useState} from 'react';
import PropTypes from "prop-types";

import ObjectGeoLocation from "forms/utils/components/ObjectGeoLocation";
import {onChange} from "forms/utils/utils";
import Container from "forms/utils/components/Container/ContainerRow";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import {DEFAULT_COUNTRY, DEFAULT_LOCATION, DEFAULT_ROUTE} from "forms/utils/constants";
import {connect} from "App/Context";

import {updateEvent} from "../actions";

const Route = ({eventRoute, updateEvent}) => {
    const [routeNumber, setRouteNumber] = useState(eventRoute.length || 1);

    const onChangeLocation = (updatedRouteNumber, path, value) => {
        const newArray = [...eventRoute].map(route => {
            if (route.routeNumber === updatedRouteNumber) {
                onChange(route, newRoute => route = newRoute)(path, value);
                return route;
            }
            return route;
        });
        updateEvent('route', newArray);
    };

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

    const addLocation = (paths, values) => {
        const nextRouteNumber = eventRoute.length + 1;
        let newElement = {...DEFAULT_ROUTE};
        newElement.location = {...DEFAULT_LOCATION};
        newElement.location.country = {...DEFAULT_COUNTRY};
        newElement.routeNumber = nextRouteNumber;
        onChange(newElement, result => newElement = result)(paths, values);
        setRouteNumber(nextRouteNumber);
        updateEvent('route', [...eventRoute, newElement])
    };

    return <Container>
        <ItemContainer>
            Add {routeNumber} route point (left click to add location):
        </ItemContainer>
            <ObjectGeoLocation
                onAdd={addLocation}
                onDelete={onDelete}
                editable={true}
                routes={eventRoute}
                onChange={onChangeLocation}
                height={400}
            />
    </Container>;
};

Route.propTypes = {
    eventRoute: PropTypes.array,
    updateEvent: PropTypes.func.isRequired
};

const mapStateToProps = () => (state) => ({
    eventRoute: state.components.forms.event.eventEdit.event.response.route
});

export default connect(mapStateToProps, {updateEvent})(Route);