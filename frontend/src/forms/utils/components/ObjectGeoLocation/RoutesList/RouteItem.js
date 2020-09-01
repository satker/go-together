import React from 'react';
import PropTypes from "prop-types";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";

import {Route} from 'forms/utils/types'
import Delete from "forms/utils/components/Icon/Delete";
import ContainerColumn from "forms/utils/components/Container/ContainerColumn";
import LeftContainer from "../../Container/LeftContainer";
import RightContainer from "../../Container/RightContainer";

const RouteItem = ({googleMap, selected, setSelected, route, onDelete}) => {
    const centerPlace = () => {
        const bounds = new googleMap.maps.LatLngBounds();
        bounds.extend(({lat: route.latitude, lng: route.longitude}));
        googleMap.map.fitBounds(bounds);
        setSelected(route.id);
    };

    return <ContainerColumn>
        <LeftContainer>
            <ListItem selected={selected === route.id} onClick={centerPlace}>
                <ListItemIcon>
                    {route.routeNumber}
                </ListItemIcon>
                <ListItemText id={"switch-list-label-" + route.routeNumber}
                              primary={route.place.name + ', ' + route.place.country.name}/>
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
    route: Route.isRequired,
    googleMap: PropTypes.object,
    setSelected: PropTypes.func.isRequired,
    onDelete: PropTypes.oneOfType([PropTypes.func, PropTypes.bool])
};

export default RouteItem;