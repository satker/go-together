import React from 'react';
import {Route} from '../../../types'
import {ListGroupItem} from 'reactstrap'
import PropTypes from "prop-types";
import DeleteButton from "../DeleteButton/DeleteButton";

const RouteItem = ({center, setCenter, route, onDelete}) => {
    console.log(center, route.latitude, route.longitude)
    return <div className='container-main-info'>
        <div className='flex' style={{width: '95%'}}>
            <ListGroupItem active={center.lat === route.latitude && center.lng === route.longitude}
                           action
                           onClick={() => setCenter([route.latitude, route.longitude])}>
                {route.routeNumber}. {route.location.name}, {route.location.country.name}
            </ListGroupItem>
        </div>
        <div className='flex' style={{width: '5%'}}>
            <DeleteButton onDelete={() => onDelete(route.routeNumber)}/>
        </div>
    </div>;
};

RouteItem.propTypes = {
    center: PropTypes.arrayOf(PropTypes.number),
    route: Route.isRequired,
    setCenter: PropTypes.func.isRequired,
    onDelete: PropTypes.func.isRequired
};

export default RouteItem;