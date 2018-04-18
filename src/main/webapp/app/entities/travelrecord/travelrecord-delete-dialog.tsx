import * as React from 'react';
import { connect } from 'react-redux';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate, ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FaBan, FaTrash } from 'react-icons/lib/fa';

import { ITravelrecord } from 'app/shared/model/travelrecord.model';
import { getEntity, deleteEntity } from './travelrecord.reducer';

export interface ITravelrecordDeleteDialogProps {
  getEntity: ICrudGetAction<ITravelrecord>;
  deleteEntity: ICrudDeleteAction<ITravelrecord>;
  travelrecord: ITravelrecord;
  match: any;
  history: any;
}

export class TravelrecordDeleteDialog extends React.Component<ITravelrecordDeleteDialogProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  confirmDelete = event => {
    this.props.deleteEntity(this.props.travelrecord.id);
    this.handleClose(event);
  };

  handleClose = event => {
    event.stopPropagation();
    this.props.history.goBack();
  };

  render() {
    const { travelrecord } = this.props;
    return (
      <Modal isOpen toggle={this.handleClose}>
        <ModalHeader toggle={this.handleClose}>
          <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
        </ModalHeader>
        <ModalBody>
          <Translate contentKey="jhipsterMonolithicSampleApp.travelrecord.delete.question" interpolate={{ id: travelrecord.id }}>
            Are you sure you want to delete this Travelrecord?
          </Translate>
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={this.handleClose}>
            <FaBan />&nbsp;
            <Translate contentKey="entity.action.cancel">Cancel</Translate>
          </Button>
          <Button color="danger" onClick={this.confirmDelete}>
            <FaTrash />&nbsp;
            <Translate contentKey="entity.action.delete">Delete</Translate>
          </Button>
        </ModalFooter>
      </Modal>
    );
  }
}

const mapStateToProps = ({ travelrecord }) => ({
  travelrecord: travelrecord.entity
});

const mapDispatchToProps = { getEntity, deleteEntity };

export default connect(mapStateToProps, mapDispatchToProps)(TravelrecordDeleteDialog);
