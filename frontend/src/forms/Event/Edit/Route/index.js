import React, {useState} from 'react';
import PropTypes from "prop-types";

import SingleMap from "forms/utils/components/ObjectGeoLocation/SingleMap";
import {onChange} from "forms/utils/utils";
import Container from "forms/utils/components/Container/ContainerRow";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import {DEFAULT_COUNTRY, DEFAULT_ROUTE, DEFAULT_ROUTE_INFO, PLACE} from "forms/utils/constants";
import {RouteInfo} from "forms/utils/types";
import {connect} from "App/Context";

import {updateEvent} from "../actions";
import RouteContent from "forms/Event/Edit/Route/Content";

export const ROUTE_INFO = 'routeInfo.infoRoutes';

const Route = ({routeInfo, updateEvent}) => {
    const [routeNumber, setRouteNumber] = useState(routeInfo.length || 1);

    const onChangeLocation = (updatedRouteNumber, path, value) => {
        const newArray = [...routeInfo.infoRoutes]
            .map(route => {
                if (route.routeNumber === updatedRouteNumber) {
                    let location = route.location;
                    onChange(location, newRoute => location = newRoute)
                    (path, value);
                    route.location = location;
                    return route;
                }
                return route;
            });
        updateEvent(ROUTE_INFO, newArray);
    };

    const onDelete = (deletedRouteNumber) => {
        const newArray = [...routeInfo.infoRoutes]
            .filter(route => route.routeNumber !== deletedRouteNumber)
            .map(route => {
                if (route.routeNumber > deletedRouteNumber) {
                    route.routeNumber = route.routeNumber - 1;
                }
                return route;
            });
        updateEvent(ROUTE_INFO, newArray);
        setRouteNumber(routeNumber - 1);
    };

    const addLocation = (paths, values) => {
        const nextRouteNumber = routeInfo.infoRoutes.length + 1;
        let newElement = {...DEFAULT_ROUTE};
        newElement.place = {...PLACE};
        newElement.place.country = {...DEFAULT_COUNTRY};

        onChange(newElement, result => newElement = result)(paths, values);

        const newRoute = DEFAULT_ROUTE_INFO(newElement, nextRouteNumber);
        setRouteNumber(nextRouteNumber);

        updateEvent(ROUTE_INFO, [...routeInfo.infoRoutes, newRoute])
    };

    return <Container>
        <ItemContainer>
            Add {routeNumber} route point (left click to add location):
        </ItemContainer>
        <SingleMap
            onAdd={addLocation}
            onDelete={onDelete}
            editable={true}
            route={routeInfo.infoRoutes.map(route => ({routeNumber: route.routeNumber, location: route.location}))}
            onChange={onChangeLocation}
            height={400}
        />
        <RouteContent/>
    </Container>;
};

Route.propTypes = {
    routeInfo: RouteInfo,
    updateEvent: PropTypes.func.isRequired
};

const mapStateToProps = (state) => ({
    routeInfo: state.components.forms.event.eventEdit.event.response.routeInfo
});

export default connect(mapStateToProps, {updateEvent})(Route);