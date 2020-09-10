import React from "react";
import List from "@material-ui/core/List";
import PropTypes from "prop-types";

import RouteItem from "./RouteItem";

const RoutesList = ({routes, selected, centerLocations}) => {
    return <List style={{overflow: 'auto'}}>
        {routes.map(route => <RouteItem key={route.name}
                                        route={route}
                                        selected={selected}
                                        centerLocations={centerLocations(route)}
        />)}
    </List>;
};

RoutesList.propTypes = {
    routes: PropTypes.array,
    selected: PropTypes.string,
    centerLocations: PropTypes.func.isRequired
};

export default RoutesList;