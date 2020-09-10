import React from "react";
import PropTypes from "prop-types";
import {navigate} from "hookrouter";
import {ListItem, ListItemIcon, ListItemText} from "@material-ui/core";

import ContainerColumn from "forms/utils/components/Container/ContainerColumn";
import RightForward from "forms/utils/components/Icon/RightForward";
import {Location} from "forms/utils/types";

const RouteItem = ({selected, route, centerLocations}) => {
    return <ContainerColumn>
        <ListItem selected={selected === route.id}
                  onClick={centerLocations}>
            <ListItemText>
                {route.name}
            </ListItemText>
            <ListItemIcon>
                <RightForward onClick={() => navigate('/events/' + route.id)}/>
            </ListItemIcon>
        </ListItem>
    </ContainerColumn>;
};

RouteItem.propTypes = {
    route: Location.isRequired,
    selected: PropTypes.string,
    centerLocations: PropTypes.func.isRequired
};

export default RouteItem;