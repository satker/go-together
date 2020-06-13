import React, {useState} from 'react';
import PropTypes from "prop-types";

import Autocomplete from '@material-ui/lab/Autocomplete';
import {withStyles} from "@material-ui/core";
import TextField from '@material-ui/core/TextField';
import CircularProgress from '@material-ui/core/CircularProgress';

import {DEFAULT_COUNTRY, DEFAULT_LOCATION, DEFAULT_ROUTE} from "forms/utils/constants";
import {getCity, getCountry, getState, requestPlaceId} from "forms/utils/components/ObjectGeoLocation/utils";
import {SimpleObject} from "forms/utils/types";

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

const AutocompleteLocation = ({setCenter, onChangeLocation, placeholder, value, setValue}) => {
    const [loading, setLoading] = useState(false);
    const [open, setOpen] = useState(false);
    const [currentOptions, setCurrentOptions] = useState([]);

    const getLocation = (paths, values) => {
        let newLocation = {...DEFAULT_ROUTE};
        newLocation = {...DEFAULT_LOCATION};
        newLocation.country = {...DEFAULT_COUNTRY};
        onChange(newLocation, result => newLocation = result)(paths, values);
        return newLocation;
    };

    const getLocationByPlaceId = ({results}) => {
        const result = results[0];
        if (onChangeLocation) {
            const newLocation = getLocation(['name', 'country.name', 'state'],
                [getCity(result), getCountry(result), getState(result)]);
            onChangeLocation(newLocation);
        }
        if (setCenter) {
            setCenter({lat: result.geometry.location.lat, lng: result.geometry.location.lng});
        }
    };

    const onChooseItem = (chooseItem) => requestPlaceId(chooseItem.id, getLocationByPlaceId);

    const setPredictions = (value) => (predictions) => {
        const result = predictions.map(prediction =>
            ({id: prediction.place_id, name: prediction.description}))
        const choose = result.filter(option => option.name === value)[0];
        if (choose) {
            if (setValue) {
                setValue(choose);
            }
            onChooseItem(choose);
        }
        setCurrentOptions(result);
        setLoading(false);
    }

    const onChange = (inputValue) => {
        if (inputValue) {
            setLoading(true);
            autocomplete.getQueryPredictions({input: inputValue, ...options}, setPredictions(inputValue));
        } else {
            setCurrentOptions([]);
        }
    };

    return (
        <StyledAutocomplete
            id={"location_autocomplete"}
            fullWidth
            open={open}
            onOpen={() => setOpen(true)}
            onClose={() => setOpen(false)}
            getOptionLabel={option => option.name}
            options={currentOptions}
            onInputChange={(evt, value) => onChange(value)}
            renderInput={params => {
                params = {
                    ...params, inputProps: {
                        ...params.inputProps,
                        value: value?.name || params.inputProps.value
                    }
                }
                return <TextField
                    {...params}
                    label={placeholder}
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
        />
    );
};

AutocompleteLocation.propTypes = {
    setCenter: PropTypes.func,
    onChangeLocation: PropTypes.func,
    setValue: PropTypes.func,
    placeholder: PropTypes.string,
    value: SimpleObject
};

AutocompleteLocation.defaultProps = {
    defaultValue: null,
    setChooseValue: () => null
}

export default AutocompleteLocation;