import React from "react";

import ContainerColumn from "../../Container/ContainerColumn";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import {Route} from "../../../types";
import PropTypes from "prop-types";

const RouteItem = ({selected, route, googleMap, setSelectedEvent}) => {
    const centerLocations = () => {
        const bounds = new googleMap.maps.LatLngBounds();
        route.locations.map(route => ({lat: route.latitude, lng: route.longitude}))
            .forEach(route => bounds.extend(route));
        googleMap.map.fitBounds(bounds);
        setSelectedEvent(route.id);
    };

    return <ContainerColumn>
        <ListItem selected={selected}
                  onClick={centerLocations}>
            <ListItemIcon>
                {route.name}
            </ListItemIcon>
        </ListItem>
    </ContainerColumn>;
};

RouteItem.propTypes = {
    route: Route.isRequired,
    onDelete: PropTypes.oneOfType([PropTypes.func, PropTypes.bool])
};

export default RouteItem;