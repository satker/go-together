import React from 'react';
import {CoordinateCenter, Route} from '../../../types'
import {ListGroupItem} from 'reactstrap'
import PropTypes from "prop-types";
import Delete from "../Icon/Delete";
import ContainerColumn from "../Container/ContainerColumn";
import LeftContainer from "../Container/LeftContainer";
import RightContainer from "../Container/RightContainer";

const RouteItem = ({center, setCenter, route, onDelete}) => {
    return <ContainerColumn>
        <LeftContainer style={{width: onDelete ? '95%' : '100%'}}>
            <ListGroupItem active={center.lat === route.latitude && center.lng === route.longitude}
                           action
                           onClick={() => setCenter([route.latitude, route.longitude])}>
                {route.routeNumber}. {route.location.name}, {route.location.country.name}
            </ListGroupItem>
        </LeftContainer>
        {onDelete && <RightContainer style={{width: '5%'}}>
            <Delete onDelete={() => onDelete(route.routeNumber)}/>
        </RightContainer>}
    </ContainerColumn>;
};

RouteItem.propTypes = {
    center: PropTypes.oneOfType([CoordinateCenter, PropTypes.arrayOf(PropTypes.number)]),
    route: Route.isRequired,
    setCenter: PropTypes.func.isRequired,
    onDelete: PropTypes.oneOfType([PropTypes.func, PropTypes.bool])
};

export default RouteItem;