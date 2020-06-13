import React, {useState} from "react";
import PropTypes from "prop-types";

import AutocompleteLocation from "forms/utils/components/AutocompleteLocation";
import CustomButton from "forms/utils/components/CustomButton";
import DeleteIcon from "forms/utils/components/Icon/Delete";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import Container from "forms/utils/components/Container/ContainerRow";

const BetweenLocations = ({onChangeLocation}) => {
    const [routes, setRoutes] = useState([]);

    const onDeleteLocation = (index) => () => {
        const updatedRoutes = routes
            .filter(route => parseInt(route.number) !== parseInt(index))
            .map(route => {
                if (parseInt(route.number) > parseInt(index)) {
                    const updatedRoute = {...route};
                    updatedRoute.number = route.number - 1;
                    return updatedRoute;
                }
                return route;
            });
        setRoutes(updatedRoutes);
        onChangeLocation(updatedRoutes);
    }

    const updateLocation = (index) => (chooseValue) => {
        const updatedLocation = routes.map(route => {
            if (route.number === index) {
                const updatedLocation = {...route};
                updatedLocation.value = chooseValue;
                return updatedLocation;
            }
            return route;
        });
        setRoutes(updatedLocation);
    };

    const updateLocationWitLatLng = (index) => ({lat, lng}) => {
        const updatedLocation = routes.map(route => {
            if (route.number === index) {
                return {...route, lat, lng};
            }
            return route;
        });
        setRoutes(updatedLocation);
        onChangeLocation(updatedLocation);
    };

    const onAddLocation = () => {
        const newLocation = {
            number: routes.length + 1
        };
        setRoutes([...routes, newLocation]);
    }

    return <Container>
        {routes.map(route => <ItemContainer>
            <AutocompleteLocation key={route.number}
                                  setCenter={updateLocationWitLatLng(route.number)}
                                  value={route.value}
                                  setValue={updateLocation(route.number)}
                                  placeholder='Middle location'
            />
            <DeleteIcon onDelete={onDeleteLocation(route.number)}/>
        </ItemContainer>)}
        <ItemContainer>
            <CustomButton onClick={onAddLocation}
                          text='Add location'/>
        </ItemContainer>
    </Container>
}

BetweenLocations.propTypes = {
    onChangeLocation: PropTypes.func.isRequired
}
export default BetweenLocations;