import React from "react";
import {NavItem, NavLink} from "reactstrap";
import classnames from "classnames";
import PropTypes from "prop-types";

const NavTab = ({name, activeTab, toggle}) => {
    return <NavItem>
        <NavLink
            className={classnames({active: activeTab === name})}
            onClick={() => toggle(name)}>
            {name}
        </NavLink>
    </NavItem>
};

NavTab.propTypes = {
    name: PropTypes.string.isRequired,
    activeTab: PropTypes.string,
    toggle: PropTypes.func.isRequired
};

export default NavTab;