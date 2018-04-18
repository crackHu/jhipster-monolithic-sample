import * as React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Button } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FaArrowLeft, FaPencil } from 'react-icons/lib/fa';

import { getEntity } from './hotnews.reducer';
import { IHotnews } from 'app/shared/model/hotnews.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IHotnewsDetailProps {
  getEntity: ICrudGetAction<IHotnews>;
  hotnews: IHotnews;
  match: any;
}

export class HotnewsDetail extends React.Component<IHotnewsDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { hotnews } = this.props;
    return (
      <div className="row justify-content-center">
        <div className="col-8">
          <h2>
            <Translate contentKey="jhipsterMonolithicSampleApp.hotnews.detail.title">Hotnews</Translate> [<b>{hotnews.id}</b>]
          </h2>
          <dl className="row-md jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="jhipsterMonolithicSampleApp.hotnews.name">Name</Translate>
              </span>
            </dt>
            <dd>{hotnews.name}</dd>
          </dl>
          <Button tag={Link} to="/entity/hotnews" replace color="info">
            <FaArrowLeft />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          <Button tag={Link} to={`/entity/hotnews/${hotnews.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ hotnews }) => ({
  hotnews: hotnews.entity
});

const mapDispatchToProps = { getEntity };

export default connect(mapStateToProps, mapDispatchToProps)(HotnewsDetail);
