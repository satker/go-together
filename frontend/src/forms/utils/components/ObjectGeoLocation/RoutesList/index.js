import React from "react";
import PropTypes from "prop-types";
import List from "@material-ui/core/List";

import RouteItem from "./RouteItem";
import {MapRoute} from "forms/utils/types";

const RoutesList = ({routes, editable, onDelete, selected, centerPlace}) => {
    return <List style={{overflow: 'auto'}}>
        {routes.map(route => <RouteItem key={route.routeNumber}
                                        onDelete={editable && (() => onDelete(route.routeNumber))}
                                        route={route}
                                        selected={selected}
                                        centerPlace={centerPlace(route)}/>)}
    </List>;
};

RoutesList.propTypes = {
    routes: PropTypes.arrayOf(MapRoute),
    editable: PropTypes.bool,
    onDelete: PropTypes.func,
    selected: PropTypes.string,
    centerPlace: PropTypes.func.isRequired
};

export default RoutesList;