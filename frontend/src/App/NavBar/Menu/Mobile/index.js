import React from "react";
import PropTypes from "prop-types";
import Menu from "@material-ui/core/Menu";
import ToolbarButtons from "App/NavBar/ToolbarButtons";

const MobileMenu = ({
                        mobileMenuId, mobileMoreAnchorEl, isMobileMenuOpen,
                        handleMobileMenuClose, handleMenuClose
                    }) => {

    return <Menu
        key={mobileMenuId + '_menu'}
        anchorEl={mobileMoreAnchorEl}
        anchorOrigin={{vertical: 'top', horizontal: 'right'}}
        id={mobileMenuId}
        keepMounted
        transformOrigin={{vertical: 'top', horizontal: 'right'}}
        open={isMobileMenuOpen}
        onClose={handleMobileMenuClose}
    >
        <ToolbarButtons handleMenuClose={handleMenuClose} menuId={mobileMenuId}/>
    </Menu>;
};

MobileMenu.propTypes = {
    mobileMenuId: PropTypes.string.isRequired,
    mobileMoreAnchorEl: PropTypes.object,
    isMobileMenuOpen: PropTypes.bool.isRequired,
    handleMobileMenuClose: PropTypes.func.isRequired,
    handleMenuClose: PropTypes.func.isRequired
};

export default MobileMenu;