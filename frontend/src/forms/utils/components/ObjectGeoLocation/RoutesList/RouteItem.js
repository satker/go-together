import React from 'react';
import PropTypes from "prop-types";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";

import {CoordinateCenter, Route} from 'forms/utils/types'
import Delete from "forms/utils/components/Icon/Delete";
import ContainerColumn from "forms/utils/components/Container/ContainerColumn";

const RouteItem = ({center, setCenter, route, onDelete}) => {
    return <ContainerColumn>
        <ListItem selected={center.lat === route.latitude && center.lng === route.longitude}
                  onClick={() => setCenter({lat: route.latitude, lng: route.longitude})}>
            <ListItemIcon>
                {route.routeNumber}
            </ListItemIcon>
            <ListItemText id={"switch-list-label-" + route.routeNumber}
                          primary={route.location.name + ', ' + route.location.country.name}/>
            {onDelete && <ListItemIcon>
                <Delete onDelete={() => onDelete(route.routeNumber)}/>
            </ListItemIcon>}
        </ListItem>
    </ContainerColumn>;
};

RouteItem.propTypes = {
    center: CoordinateCenter,
    route: Route.isRequired,
    setCenter: PropTypes.func.isRequired,
    onDelete: PropTypes.oneOfType([PropTypes.func, PropTypes.bool])
};

export default RouteItem;