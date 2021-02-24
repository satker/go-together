import React from 'react';
import PropTypes from "prop-types";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";

import {MapRoute} from 'forms/utils/types'
import Delete from "forms/utils/components/Icon/Delete";
import ContainerColumn from "forms/utils/components/Container/ContainerColumn";
import LeftContainer from "forms/utils/components/Container/LeftContainer";
import RightContainer from "forms/utils/components/Container/RightContainer";

const RouteItem = ({selected, centerPlace, route, onDelete}) => {
    return <ContainerColumn>
        <LeftContainer>
            <ListItem selected={selected === route.location.id} onClick={centerPlace}>
                <ListItemIcon>
                    {route.routeNumber}
                </ListItemIcon>
                <ListItemText id={"switch-list-label-" + route.routeNumber}
                              primary={route.location.place.name + ', ' + route.location.place.country.name}/>
            </ListItem>
        </LeftContainer>
        <RightContainer>
            {onDelete && <ListItemIcon>
                <Delete onDelete={onDelete}/>
            </ListItemIcon>}
        </RightContainer>
    </ContainerColumn>;
};

RouteItem.propTypes = {
    selected: PropTypes.string,
    route: MapRoute.isRequired,
    centerPlace: PropTypes.func.isRequired,
    onDelete: PropTypes.oneOfType([PropTypes.func, PropTypes.bool])
};

export default RouteItem;