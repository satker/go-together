import React from "react";

import List from "@material-ui/core/List";
import RouteItem from "./RouteItem";
import PropTypes from "prop-types";

const RoutesList = ({routes, googleMap, selectedEvent, setSelectedEvent}) => {

    return <List style={{overflow: 'auto'}}>
        {routes.map(route => <RouteItem key={route.name}
                                        route={route}
                                        googleMap={googleMap}
                                        selected={selectedEvent === route.id}
                                        setSelectedEvent={setSelectedEvent}
        />)}
    </List>;
};

RoutesList.propTypes = {
    routes: PropTypes.array,
    editable: PropTypes.bool,
    onDelete: PropTypes.func
};

export default RoutesList;