import React, {useEffect, useState} from 'react';
import PropTypes from "prop-types";

import Autocomplete from '@material-ui/lab/Autocomplete';
import {withStyles} from "@material-ui/core";
import TextField from '@material-ui/core/TextField';
import CircularProgress from '@material-ui/core/CircularProgress';

import {DEFAULT_COUNTRY, DEFAULT_LOCATION, DEFAULT_ROUTE} from "forms/utils/constants";
import {getCity, getCountry, getState, requestPlaceId} from "forms/utils/components/ObjectGeoLocation/utils";
import {onChange} from "forms/utils/utils";

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

const AutocompleteLocation = ({setCenter, onChangeLocation, placeholder, defaultValue, setChooseValue}) => {
    const [value, setValue] = useState('');
    const [loading, setLoading] = useState(false);
    const [open, setOpen] = useState(false);
    const [chooseItem, setChooseItem] = useState(null);
    const [currentOptions, setCurrentOptions] = useState([]);

    const getLocation = (paths, values) => {
        let newLocation = {...DEFAULT_ROUTE};
        newLocation = {...DEFAULT_LOCATION};
        newLocation.country = {...DEFAULT_COUNTRY};
        onChange(newLocation, result => newLocation = result)(paths, values);
        return newLocation;
    };

    useEffect(() => {
        if (defaultValue) {
            setValue(defaultValue)
        }
    }, [defaultValue])

    useEffect(() => {
        if (chooseItem) {
            requestPlaceId(chooseItem.id, ({results}) => {
                const result = results[0];
                if (onChangeLocation) {
                    const newLocation = getLocation(['name', 'country.name', 'state'],
                        [getCity(result), getCountry(result), getState(result)]);
                    onChangeLocation(newLocation);
                }
                if (setCenter) {
                    setCenter({lat: result.geometry.location.lat, lng: result.geometry.location.lng});
                }
            })
        }
    }, [chooseItem]);

    useEffect(() => {
        const setPredictions = (predictions) => {
            const result = predictions.map(prediction =>
                ({id: prediction.place_id, name: prediction.description}))
            const choose = result.filter(option => option.name === value)[0];
            if (choose) {
                if (setChooseValue) {
                    setChooseValue(choose);
                }
                setChooseItem(choose);
            }
            setCurrentOptions(result);
            setLoading(false);
        }
        if (value) {
            setLoading(true);
            autocomplete.getQueryPredictions({input: value, ...options}, setPredictions);
        } else {
            setCurrentOptions([]);
        }
    }, [value]);

    return (
        <StyledAutocomplete
            id={"location_autocomplete"}
            fullWidth
            open={open}
            onOpen={() => setOpen(true)}
            onClose={() => setOpen(false)}
            getOptionLabel={option => option.name}
            options={currentOptions}
            onInputChange={(evt, value) => setValue(value)}
            renderInput={params => {
                params = {
                    ...params, inputProps: {
                        ...params.inputProps,
                        value: defaultValue?.name || params.inputProps.value
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
    setChooseValue: PropTypes.func
};

AutocompleteLocation.defaultProps = {
    defaultValue: null,
    setChooseValue: () => null
}

export default AutocompleteLocation;