import React from "react";
import PropTypes from "prop-types";
import {ResponseData} from '../../types';
import Loading from "./Loading";
import ErrorMessage from "./ErrorMessage";
import {isEmpty} from "lodash";

const LoadableContent = ({children, loadableData, loadingMessage}) => {
    if (loadableData.inProcess || isEmpty(loadableData.response || loadableData.response.length === 0)) {
        return <Loading loadingMessage={loadingMessage}/>
    }

    if (loadableData.error) {
        return <ErrorMessage error={loadableData.error}/>;
    }

    return children;
};

LoadableContent.propTypes = {
    children: PropTypes.node,
    loadableData: ResponseData.isRequired,
    loadingMessage: PropTypes.string
};

export default LoadableContent;