import React from "react";
import List from "@material-ui/core/List";
import PropTypes from "prop-types";

import RouteItem from "./RouteItem";

const RoutesList = ({routes, googleMap, selectedEvent, setSelectedEvent}) => {
    console.log(selectedEvent, routes)
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