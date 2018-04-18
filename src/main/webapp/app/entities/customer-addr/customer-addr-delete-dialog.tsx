import * as React from 'react';
import { connect } from 'react-redux';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate, ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FaBan, FaTrash } from 'react-icons/lib/fa';

import { ICustomerAddr } from 'app/shared/model/customer-addr.model';
import { getEntity, deleteEntity } from './customer-addr.reducer';

export interface ICustomerAddrDeleteDialogProps {
  getEntity: ICrudGetAction<ICustomerAddr>;
  deleteEntity: ICrudDeleteAction<ICustomerAddr>;
  customerAddr: ICustomerAddr;
  match: any;
  history: any;
}

export class CustomerAddrDeleteDialog extends React.Component<ICustomerAddrDeleteDialogProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  confirmDelete = event => {
    this.props.deleteEntity(this.props.customerAddr.id);
    this.handleClose(event);
  };

  handleClose = event => {
    event.stopPropagation();
    this.props.history.goBack();
  };

  render() {
    const { customerAddr } = this.props;
    return (
      <Modal isOpen toggle={this.handleClose}>
        <ModalHeader toggle={this.handleClose}>
          <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
        </ModalHeader>
        <ModalBody>
          <Translate contentKey="jhipsterMonolithicSampleApp.customerAddr.delete.question" interpolate={{ id: customerAddr.id }}>
            Are you sure you want to delete this CustomerAddr?
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

const mapStateToProps = ({ customerAddr }) => ({
  customerAddr: customerAddr.entity
});

const mapDispatchToProps = { getEntity, deleteEntity };

export default connect(mapStateToProps, mapDispatchToProps)(CustomerAddrDeleteDialog);
