import React from "react";
import PropTypes from "prop-types";
import List from "@material-ui/core/List";

import RouteItem from "./RouteItem";

const RoutesList = ({routes, editable, onDelete, googleMap, selected, setSelected}) => {
    return <List style={{overflow: 'auto'}}>
        {routes.map(route => <RouteItem key={route.routeNumber}
                                        onDelete={editable && (() => onDelete(route.routeNumber))}
                                        route={route}
                                        googleMap={googleMap}
                                        selected={selected}
                                        setSelected={setSelected}/>)}
    </List>;
};

RoutesList.propTypes = {
    routes: PropTypes.array,
    editable: PropTypes.bool,
    googleMap: PropTypes.object,
    onDelete: PropTypes.func,
    selected: PropTypes.string,
    setSelected: PropTypes.func.isRequired
};

export default RoutesList;