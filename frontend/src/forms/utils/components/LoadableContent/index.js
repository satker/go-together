import React from "react";
import PropTypes from "prop-types";
import {isEmpty} from "lodash";
import FormHelperText from '@material-ui/core/FormHelperText';

import {ResponseData} from 'forms/utils/types';

import Loading from "./Loading";

const LoadableContent = ({children, loadableData, loadingMessage, additionalCheck}) => {
    if (loadableData.inProcess ||
        isEmpty(loadableData.response) ||
        loadableData.response.length === 0 ||
        (additionalCheck ? additionalCheck(loadableData.response) : false)
    ) {
        return <Loading loadingMessage={loadingMessage}/>
    }

    if (loadableData.error) {
        return <FormHelperText error>{loadableData.error}</FormHelperText>;
    }

    return children;
};

LoadableContent.propTypes = {
    children: PropTypes.node,
    loadableData: ResponseData.isRequired,
    loadingMessage: PropTypes.string,
    additionalCheck: PropTypes.func
};

export default LoadableContent;