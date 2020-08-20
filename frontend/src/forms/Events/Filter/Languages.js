import React, {useEffect, useState} from 'react';
import PropTypes from "prop-types";

import {connect} from "App/Context";

import MultipleSelectBox from "forms/utils/components/MultipleSelectBox";
import LoadableContent from "forms/utils/components/LoadableContent";
import {FilterOperator} from "forms/utils/utils";

import {getLanguages, setFilter} from "../actions";
import {ResponseData} from "forms/utils/types";

const Languages = ({languages, getLanguages, setFilter}) => {
    const [chooseLanguages, setChooseLanguages] = useState([]);

    useEffect(() => {
        getLanguages();
    }, [getLanguages]);

    const onChange = (languages) => {
        const searchField = "author?languages.id";
        setChooseLanguages(languages);
        let searchLanguages;
        if (languages.length === 0) {
            searchLanguages = null;
        } else {
            searchLanguages = [{
                "id": languages.map(language => language.id)
            }];
        }
        setFilter(FilterOperator.IN,
            searchLanguages,
            searchField, languages.length);
    }

    return <LoadableContent loadableData={languages}>
        <MultipleSelectBox onChange={onChange}
                           value={chooseLanguages}
                           optionsSimple={languages.response.result}
                           label='Languages'/>
    </LoadableContent>;
};

Languages.propTypes = {
    setFilter: PropTypes.func.isRequired,
    getLanguages: PropTypes.func.isRequired,
    languages: ResponseData
}

const mapStateToProps = state => ({
    languages: state.components.forms.events.languages
});

export default connect(mapStateToProps, {getLanguages, setFilter})(Languages);