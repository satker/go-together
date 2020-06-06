import React, {useState} from "react";
import PropTypes from "prop-types";

import AutocompleteLocation from "forms/utils/components/AutocompleteLocation";
import CustomButton from "forms/utils/components/CustomButton";
import {FilterOperator} from "forms/utils/utils";
import {SearchObject} from "forms/utils/types";
import DeleteIcon from "forms/utils/components/Icon/Delete";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import Container from "forms/utils/components/Container/ContainerRow";

const LocationField = 'route?[routeNumber&latitude,longitude]';
const latLng = "latitude,longitude";

const BetweenLocations = ({updateFilterObject, filterObject}) => {
    const [routes, setRoutes] = useState([]);

    let filteredLocations = filterObject.filters[LocationField]?.values;

    const onDeleteLocation = (index) => () => {
        console.log('onDeleteLocation', routes, index)
        if (filteredLocations) {
            const newValue = filteredLocations
                .filter(value => value.routeNumber !== index)
                .map(value => {
                    if (value.routeNumber > index) {
                        return {...value, routeNumber: value.routeNumber - 1}
                    }
                    return value;
                })
            updateFilterObject(FilterOperator.NEAR_LOCATION, newValue, LocationField, true);
        }
        let lastKey = null;
        const updatedRoutes = {...routes}
        routes
            .filter(routeNumber => parseInt(routeNumber) > parseInt(index))
            .forEach(routeNumber => {
                updatedRoutes[routeNumber - 1] = routes[routeNumber];
                lastKey = routeNumber;
            });
        if (lastKey) {
            delete updatedRoutes[lastKey];
        }
        setRoutes(updatedRoutes);
    }

    const onAddLocation = () => {
        const index = routes.length + 1;
        setRoutes([...routes, index]);
    }
    console.log('routes', routes)

    return <Container>
        {routes.map(index => <ItemContainer>
            <AutocompleteLocation key={index} setCenter={updateFilterObject}/>
            <DeleteIcon onDelete={onDeleteLocation(index)}/>
        </ItemContainer>)}
        <ItemContainer>
            <CustomButton onClick={onAddLocation}
                          text='Add location'/>
        </ItemContainer>
    </Container>
}

BetweenLocations.propTypes = {
    updateFilterObject: PropTypes.func.isRequired,
    filterObject: SearchObject.isRequired
}
export default BetweenLocations;