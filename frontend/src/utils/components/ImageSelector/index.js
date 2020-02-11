import React from 'react';
import PropTypes from 'prop-types';
import {FormGroup, Input} from "reactstrap";
import {Switch} from "@material-ui/core";

const ImageSelector = ({
                           isUrlPhoto, setIsUrlPhoto, onPhotoUrlChangeHandler, onFileChangeHandler,
                           multiple
                       }) =>
    <FormGroup>
        Way to add photo by {isUrlPhoto ? ' URL' : ' file'}<Switch checked={isUrlPhoto}
                                                                   onChange={() => setIsUrlPhoto(!isUrlPhoto)}
                                                                   value='photo'
    />
        {isUrlPhoto !== null ? (isUrlPhoto ?
            <Input type="text"
                   name="photoUrl"
                   id="photoUrl"
                   placeholder="http://photo.com/photo.jpg"
                   onChange={onPhotoUrlChangeHandler}/>
            : <Input type="file"
                     multiple={multiple}
                     name="file"
                     onChange={onFileChangeHandler}/>) : null}
    </FormGroup>;

ImageSelector.props = {
    isUrlPhoto: PropTypes.bool.isRequired,
    setIsUrlPhoto: PropTypes.func.isRequired,
    onPhotoUrlChangeHandler: PropTypes.func.isRequired,
    onFileChangeHandler: PropTypes.func.isRequired,
    multiple: PropTypes.bool.isRequired
};

export default ImageSelector;