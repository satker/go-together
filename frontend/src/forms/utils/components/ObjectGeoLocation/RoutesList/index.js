import React from "react";
import PropTypes from "prop-types";
import List from "@material-ui/core/List";

import {CoordinateCenter} from "forms/utils/types";

import RouteItem from "./RouteItem";

const RoutesList = ({routes, editable, onDelete, center, setCenter}) => {
    return <List style={{overflow: 'auto'}}>
        {routes.map(route => <RouteItem key={route.routeNumber}
                                        onDelete={editable && ((routeNumber) => {
                                            onDelete(routeNumber);
                                        })}
                                        route={route}
                                        center={center}
                                        setCenter={setCenter}/>)}
    </List>;
};

RoutesList.propTypes = {
    routes: PropTypes.array,
    editable: PropTypes.bool,
    onDelete: PropTypes.func,
    center: CoordinateCenter,
    setCenter: PropTypes.func.isRequired
};

export default RoutesList;