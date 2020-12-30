import React, {useEffect} from "react";
import {connect} from "App/Context";
import {getTransportTypes, updateEvent} from "forms/Event/Edit/actions";
import {RouteInfo} from "forms/utils/types";
import PropTypes from "prop-types";
import RouteItem from "forms/Event/Edit/Route/Row";
import {onChange} from "forms/utils/utils";
import {ROUTE_INFO} from "forms/Event/Edit/Route/index";

const RouteContent = ({routeInfo, updateEvent, getTransportTypes}) => {
    useEffect(() => {
        getTransportTypes()
    }, [getTransportTypes]);

    const onChangeRouteInfo = (routeNumber) => (path, value) => {
        const newArray = routeInfo.infoRoutes.map(route => {
            if (route.routeNumber === routeNumber) {
                onChange(route, newRoute => route = newRoute)(path, value);
            }
            console.log(route, value)
            return route;
        });
        updateEvent(ROUTE_INFO, newArray);
    }

    return routeInfo.infoRoutes.map(route => <RouteItem key={route.routeNumber}
                                                        route={route}
                                                        onChange={onChangeRouteInfo(route.routeNumber)}/>)
}

const mapStateToProps = (state) => ({
    routeInfo: state.components.forms.event.eventEdit.event.response.routeInfo
});

RouteContent.propTypes = {
    routeInfo: RouteInfo,
    updateEvent: PropTypes.func.isRequired
};

export default connect(mapStateToProps, {updateEvent, getTransportTypes})(RouteContent);