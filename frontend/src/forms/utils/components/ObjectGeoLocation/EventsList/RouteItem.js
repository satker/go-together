import React from "react";
import PropTypes from "prop-types";
import {navigate} from "hookrouter";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";

import ContainerColumn from "forms/utils/components/Container/ContainerColumn";
import RightForward from "forms/utils/components/Icon/RightForward";
import {Route} from "forms/utils/types";

const RouteItem = ({selected, route, googleMap, setSelected}) => {
    const centerLocations = () => {
        const bounds = new googleMap.maps.LatLngBounds();
        route.locations.map(route => ({lat: route.latitude, lng: route.longitude}))
            .forEach(route => bounds.extend(route));
        googleMap.map.fitBounds(bounds);
        setSelected(route.id);
    };

    return <ContainerColumn>
        <ListItem selected={selected === route.id}
                  onClick={centerLocations}>
            <ListItemIcon>
                {route.name}
            </ListItemIcon>
            <ListItemIcon>
                <RightForward onClick={() => navigate('/events/' + route.id)}/>
            </ListItemIcon>
        </ListItem>
    </ContainerColumn>;
};

RouteItem.propTypes = {
    route: Route.isRequired,
    onDelete: PropTypes.oneOfType([PropTypes.func, PropTypes.bool])
};

export default RouteItem;