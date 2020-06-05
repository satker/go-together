import React, {useState} from "react";
import PropTypes from "prop-types";
import {keys, values} from "lodash";

import AutocompleteLocation from "forms/utils/components/AutocompleteLocation";
import CustomButton from "forms/utils/components/CustomButton";
import {FilterOperator} from "forms/utils/utils";
import {SearchObject} from "forms/utils/types";
import DeleteIcon from "forms/utils/components/Icon/Delete";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import Container from "forms/utils/components/Container/ContainerRow";

const LocationField = 'route?[routeNumber&latitude&longitude]';

const Locations = ({updateFilterObject, filterObject}) => {
    const [routes, setRoutes] = useState({});

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
            updateFilterObject(FilterOperator.EQUAL, newValue, LocationField, true);
        }
        let lastKey = null;
        const updatedRoutes = {...routes}
        keys(routes)
            .filter(routeNumber => parseInt(routeNumber) > parseInt(index))
            .forEach(routeNumber => {
                console.log(updatedRoutes);
                updatedRoutes[routeNumber - 1] = routes[routeNumber];
                lastKey = routeNumber;
            });
        if (lastKey) {
            delete updatedRoutes[lastKey];
        }
        setRoutes(updatedRoutes);
    }

    const onChangeLocation = (index) => (location) => {
        const newElement = {
            routeNumber: index,
            latitude: location.lat,
            longitude: location.lng
        };
        if (location) {
            if (filteredLocations) {
                filteredLocations.push(newElement)
            } else {
                filteredLocations = [newElement]
            }
        }
        updateFilterObject(FilterOperator.EQUAL, filteredLocations, LocationField, true);
    }

    const onAddLocation = () => {
        const index = keys(routes).length + 1;
        const newLocation = <ItemContainer>
            <AutocompleteLocation key={index} setCenter={onChangeLocation(index)}/>
            <DeleteIcon onDelete={onDeleteLocation(index)}/>
        </ItemContainer>
        setRoutes({...routes, [index]: newLocation});
    }

    return <Container>
        {values(routes)}
        <ItemContainer>
            <CustomButton onClick={onAddLocation}
                          text='Add location'/>
        </ItemContainer>
    </Container>
}

Locations.propTypes = {
    updateFilterObject: PropTypes.func.isRequired,
    filterObject: SearchObject.isRequired
}
export default Locations;