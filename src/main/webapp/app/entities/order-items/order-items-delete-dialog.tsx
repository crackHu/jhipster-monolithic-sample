import * as React from 'react';
import { connect } from 'react-redux';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate, ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FaBan, FaTrash } from 'react-icons/lib/fa';

import { IOrderItems } from 'app/shared/model/order-items.model';
import { getEntity, deleteEntity } from './order-items.reducer';

export interface IOrderItemsDeleteDialogProps {
  getEntity: ICrudGetAction<IOrderItems>;
  deleteEntity: ICrudDeleteAction<IOrderItems>;
  orderItems: IOrderItems;
  match: any;
  history: any;
}

export class OrderItemsDeleteDialog extends React.Component<IOrderItemsDeleteDialogProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  confirmDelete = event => {
    this.props.deleteEntity(this.props.orderItems.id);
    this.handleClose(event);
  };

  handleClose = event => {
    event.stopPropagation();
    this.props.history.goBack();
  };

  render() {
    const { orderItems } = this.props;
    return (
      <Modal isOpen toggle={this.handleClose}>
        <ModalHeader toggle={this.handleClose}>
          <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
        </ModalHeader>
        <ModalBody>
          <Translate contentKey="jhipsterMonolithicSampleApp.orderItems.delete.question" interpolate={{ id: orderItems.id }}>
            Are you sure you want to delete this OrderItems?
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

const mapStateToProps = ({ orderItems }) => ({
  orderItems: orderItems.entity
});

const mapDispatchToProps = { getEntity, deleteEntity };

export default connect(mapStateToProps, mapDispatchToProps)(OrderItemsDeleteDialog);
