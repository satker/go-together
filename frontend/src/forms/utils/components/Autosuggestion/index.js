import React, {useEffect, useState} from 'react';
import PropTypes from 'prop-types';
import TextField from '@material-ui/core/TextField';
import Autocomplete from '@material-ui/lab/Autocomplete';
import CircularProgress from '@material-ui/core/CircularProgress';
import {withStyles} from "@material-ui/core";

import {connect} from "App/Context";
import {ResponseData} from "forms/utils/types";

import {getOptions} from "./actions";
import {AUTOSUGGESTION_OPTIONS_EVENTS, AUTOSUGGESTION_OPTIONS_LOCATIONS} from "./constants";

const StyledAutocomplete = withStyles({
    inputRoot: {
        backgroundColor: 'white'
    }
})(Autocomplete);

const IntegrationAutosuggest = ({formId, setResult, placeholder, url, urlParam, getOptions, options}) => {
    const [open, setOpen] = useState(false);
    const [value, setValue] = useState('');
    const [chooseItem, setChooseItem] = useState(null);
    const [currentOptions, setCurrentOptions] = useState([]);

    useEffect(() => {
        if (options) {
            if (!options.inProcess) {
                setChooseItem(options.response.filter(option => option.name === value)[0]);
                setCurrentOptions(options.response);
            }
        }
    }, [options, value, setCurrentOptions]);

    useEffect(() => {
        if (chooseItem) {
            setResult(chooseItem);
        }
    }, [chooseItem, value, setResult]);

    useEffect(() => {
        if (!chooseItem) {
            getOptions(url, urlParam, value);
        }
    }, [chooseItem, getOptions, value, url, urlParam]);

    return (
        <StyledAutocomplete
            id={formId + "autocomplete"}
            fullWidth
            open={open}
            onOpen={() => {
                setOpen(true);
            }}
            onClose={() => {
                setOpen(false);
            }}
            getOptionLabel={option => option.name}
            options={currentOptions}
            loading={options.inProcess}
            onInputChange={(evt, value) => setValue(value)}
            renderInput={params => <TextField
                {...params}
                label={placeholder}
                fullWidth
                variant="outlined"
                InputProps={{
                    ...params.InputProps,
                    endAdornment: (
                        <>
                            {options.inProcess ? <CircularProgress
                                color="inherit"
                                size={20}/> : null}
                            {params.InputProps.endAdornment}
                        </>
                    ),
                }}
            />
            }
        />
    );
};


IntegrationAutosuggest.propTypes = {
    setResult: PropTypes.func.isRequired,
    placeholder: PropTypes.string.isRequired,
    url: PropTypes.string.isRequired,
    urlParam: PropTypes.string.isRequired,
    formId: PropTypes.string.isRequired,
    getOptions: PropTypes.func.isRequired,
    options: ResponseData.isRequired
};

const mapStateToProps = (autosuggestionOptions) => () => state => ({
    options: state.components.utils.autosuggestion.options[autosuggestionOptions]
});

export const AutosuggestionEvents = connect(mapStateToProps('events'), {
    getOptions: getOptions(AUTOSUGGESTION_OPTIONS_EVENTS)
}, 'AutosuggestionEvents')(IntegrationAutosuggest);

export const AutosuggestionLocations = connect(mapStateToProps('locations'), {
    getOptions: getOptions(AUTOSUGGESTION_OPTIONS_LOCATIONS)
}, 'AutosuggestionLocations')(IntegrationAutosuggest);