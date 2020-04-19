import React, {useEffect, useState} from 'react';
import PropTypes from 'prop-types';
import TextField from '@material-ui/core/TextField';
import Autocomplete from '@material-ui/lab/Autocomplete';
import CircularProgress from '@material-ui/core/CircularProgress';
import {connect} from "../../../../App/Context";
import {withStyles} from "@material-ui/core";
import {getOptions} from "./actions";

const StyledAutocomplete = withStyles({
    inputRoot: {
        flexWrap: 'inherit',
        padding: '14px 6px'
    },
    input: {
        height: '28px'
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
            style={{width: 400}}
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
    options: PropTypes.array.isRequired
};

const mapStateToProps = (FORM_ID) => state => ({
    options: state[FORM_ID]?.options || []
});

export default connect(mapStateToProps, {getOptions})(IntegrationAutosuggest);