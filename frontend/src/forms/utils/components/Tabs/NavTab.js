import React from "react";
import PropTypes from "prop-types";
import Tab from "@material-ui/core/Tab";

const a11yProps = (index) => {
    return {
        id: `scrollable-auto-tab-${index}`,
        'aria-controls': `scrollable-auto-tabpanel-${index}`,
    };
};

const NavTab = ({name, index, onChange}) => {
    return <Tab label={name} value={index} onChange={onChange} {...a11yProps(index)}/>
};

NavTab.propTypes = {
    name: PropTypes.string.isRequired,
    index: PropTypes.number,
    onChange: PropTypes.func.isRequired
};

export default NavTab;