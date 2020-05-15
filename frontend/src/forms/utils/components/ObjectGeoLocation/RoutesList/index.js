import React from "react";
import ListSubheader from "@material-ui/core/ListSubheader";
import RouteItem from "./RouteItem";
import List from "@material-ui/core/List";
import PropTypes from "prop-types";

const RoutesList = ({routes, editable, onDelete, center, setCenter}) => {
    return <List subheader={<ListSubheader>Routes</ListSubheader>}>
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
    center: PropTypes.arrayOf(PropTypes.number),
    setCenter: PropTypes.func.isRequired
};

export default RoutesList;