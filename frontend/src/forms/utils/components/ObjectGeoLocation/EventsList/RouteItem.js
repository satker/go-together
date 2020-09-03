import React from "react";
import PropTypes from "prop-types";
import {navigate} from "hookrouter";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";

import ContainerColumn from "forms/utils/components/Container/ContainerColumn";
import RightForward from "forms/utils/components/Icon/RightForward";
import {Route} from "forms/utils/types";

const RouteItem = ({selected, route, centerLocations}) => {
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
    selected: PropTypes.string,
    centerLocations: PropTypes.func.isRequired
};

export default RouteItem;