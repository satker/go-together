import React, {useState} from 'react';
import PropTypes from "prop-types";

import Autocomplete from '@material-ui/lab/Autocomplete';
import {withStyles} from "@material-ui/core";
import TextField from '@material-ui/core/TextField';
import CircularProgress from '@material-ui/core/CircularProgress';

import {onChange} from "forms/utils/utils";
import {DEFAULT_COUNTRY, DEFAULT_ROUTE, PLACE} from "forms/utils/constants";
import {getCity, getCountry, getState, requestPlaceId} from "forms/utils/components/ObjectGeoLocation/utils";
import {Place} from "forms/utils/types";

const google = window.google;

const StyledAutocomplete = withStyles({
    inputRoot: {
        backgroundColor: 'white'
    }
})(Autocomplete);

const autocomplete = new google.maps.places.AutocompleteService();

const options = {
    types: ['(cities)']
};

const AutocompleteLocation = ({setCenter, onChangeLocation, placeholder, value, setValueCenter, error}) => {
    const [loading, setLoading] = useState(false);
    const [open, setOpen] = useState(false);
    const [currentOptions, setCurrentOptions] = useState([]);

    const getLocation = (paths, values) => {
        let newLocation = {...DEFAULT_ROUTE};
        newLocation = {...PLACE};
        newLocation.country = {...DEFAULT_COUNTRY};
        onChange(newLocation, result => newLocation = result)(paths, values);
        return newLocation;
    };

    const getLocationByPlaceId = (value) => ({results}) => {
        const result = results[0];
        if (onChangeLocation) {
            const newLocation = getLocation(['name', 'country.name', 'state'],
                [getCity(result), getCountry(result), getState(result)]);
            onChangeLocation(newLocation, {lat: result.geometry.location.lat, lng: result.geometry.location.lng});
        }
        if (setCenter) {
            setCenter({lat: result.geometry.location.lat, lng: result.geometry.location.lng});
        } else if (setValueCenter) {
            setValueCenter({value, lat: result.geometry.location.lat, lng: result.geometry.location.lng})
        }
    };

    const onChooseItem = (chooseItem) => requestPlaceId(chooseItem, getLocationByPlaceId);

    const setPredictions = (value) => (predictions) => {
        const result = predictions.map(prediction =>
            ({id: prediction.place_id, name: prediction.description}))
        const choose = result.filter(option => option.name === value)[0];
        if (choose) {
            onChooseItem(choose);
        }
        setCurrentOptions(result);
        setLoading(false);
    }

    const onChangeValue = (inputValue) => {
        setValueCenter && setValueCenter({value: {name: inputValue}});

        if (inputValue) {
            setLoading(true);
            autocomplete.getQueryPredictions({input: inputValue, ...options}, setPredictions(inputValue));
        } else {
            setCurrentOptions([]);
        }
    };

    return <StyledAutocomplete
        id={"location_autocomplete"}
        fullWidth
        open={open}
        onOpen={() => setOpen(true)}
        onClose={() => setOpen(false)}
        getOptionLabel={option => option.name}
        options={currentOptions}
        onInputChange={(evt, value) => onChangeValue(value)}
        renderInput={params => {
            params = {
                    ...params, inputProps: {
                        ...params.inputProps,
                    value: setValueCenter ? value?.name || '' : params.inputProps.value
                    }
                }
                return <TextField
                    {...params}
                    label={placeholder}
                    error={!!error}
                    helperText={error}
                    fullWidth
                    variant="outlined"
                    InputProps={{
                        ...params.InputProps,
                        endAdornment: (
                            <>
                                {loading ? <CircularProgress
                                    color="inherit"
                                    size={20}/> : null}
                                {params.InputProps.endAdornment}
                            </>
                        ),
                    }}
                />
        }
        }
    />;
};

AutocompleteLocation.propTypes = {
    setCenter: PropTypes.func,
    onChangeLocation: PropTypes.func,
    setValueCenter: PropTypes.func,
    placeholder: PropTypes.string,
    value: Place,
    error: PropTypes.string
};

AutocompleteLocation.defaultProps = {
    defaultValue: null,
}

export default AutocompleteLocation;