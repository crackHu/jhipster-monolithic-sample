import * as React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Button } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FaArrowLeft, FaPencil } from 'react-icons/lib/fa';

import { getEntity } from './order-items.reducer';
import { IOrderItems } from 'app/shared/model/order-items.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrderItemsDetailProps {
  getEntity: ICrudGetAction<IOrderItems>;
  orderItems: IOrderItems;
  match: any;
}

export class OrderItemsDetail extends React.Component<IOrderItemsDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { orderItems } = this.props;
    return (
      <div className="row justify-content-center">
        <div className="col-8">
          <h2>
            <Translate contentKey="jhipsterMonolithicSampleApp.orderItems.detail.title">OrderItems</Translate> [<b>{orderItems.id}</b>]
          </h2>
          <dl className="row-md jh-entity-details">
            <dt>
              <span id="orderId">
                <Translate contentKey="jhipsterMonolithicSampleApp.orderItems.orderId">Order Id</Translate>
              </span>
            </dt>
            <dd>{orderItems.orderId}</dd>
          </dl>
          <Button tag={Link} to="/entity/order-items" replace color="info">
            <FaArrowLeft />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          <Button tag={Link} to={`/entity/order-items/${orderItems.id}/edit`} replace color="primary">
            <FaPencil />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ orderItems }) => ({
  orderItems: orderItems.entity
});

const mapDispatchToProps = { getEntity };

export default connect(mapStateToProps, mapDispatchToProps)(OrderItemsDetail);
