import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';
import { ICourseEnrollment } from '../course-enrollment.model';
import { CourseEnrollmentService } from '../service/course-enrollment.service';
import { CourseEnrollmentFormService } from './course-enrollment-form.service';

import { CourseEnrollmentUpdateComponent } from './course-enrollment-update.component';

describe('CourseEnrollment Management Update Component', () => {
  let comp: CourseEnrollmentUpdateComponent;
  let fixture: ComponentFixture<CourseEnrollmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let courseEnrollmentFormService: CourseEnrollmentFormService;
  let courseEnrollmentService: CourseEnrollmentService;
  let courseService: CourseService;
  let studentService: StudentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CourseEnrollmentUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CourseEnrollmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CourseEnrollmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    courseEnrollmentFormService = TestBed.inject(CourseEnrollmentFormService);
    courseEnrollmentService = TestBed.inject(CourseEnrollmentService);
    courseService = TestBed.inject(CourseService);
    studentService = TestBed.inject(StudentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Course query and add missing value', () => {
      const courseEnrollment: ICourseEnrollment = { id: 8188 };
      const course: ICourse = { id: 2858 };
      courseEnrollment.course = course;

      const courseCollection: ICourse[] = [{ id: 2858 }];
      jest.spyOn(courseService, 'query').mockReturnValue(of(new HttpResponse({ body: courseCollection })));
      const additionalCourses = [course];
      const expectedCollection: ICourse[] = [...additionalCourses, ...courseCollection];
      jest.spyOn(courseService, 'addCourseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ courseEnrollment });
      comp.ngOnInit();

      expect(courseService.query).toHaveBeenCalled();
      expect(courseService.addCourseToCollectionIfMissing).toHaveBeenCalledWith(
        courseCollection,
        ...additionalCourses.map(expect.objectContaining),
      );
      expect(comp.coursesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Student query and add missing value', () => {
      const courseEnrollment: ICourseEnrollment = { id: 8188 };
      const student: IStudent = { id: 9978 };
      courseEnrollment.student = student;

      const studentCollection: IStudent[] = [{ id: 9978 }];
      jest.spyOn(studentService, 'query').mockReturnValue(of(new HttpResponse({ body: studentCollection })));
      const additionalStudents = [student];
      const expectedCollection: IStudent[] = [...additionalStudents, ...studentCollection];
      jest.spyOn(studentService, 'addStudentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ courseEnrollment });
      comp.ngOnInit();

      expect(studentService.query).toHaveBeenCalled();
      expect(studentService.addStudentToCollectionIfMissing).toHaveBeenCalledWith(
        studentCollection,
        ...additionalStudents.map(expect.objectContaining),
      );
      expect(comp.studentsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const courseEnrollment: ICourseEnrollment = { id: 8188 };
      const course: ICourse = { id: 2858 };
      courseEnrollment.course = course;
      const student: IStudent = { id: 9978 };
      courseEnrollment.student = student;

      activatedRoute.data = of({ courseEnrollment });
      comp.ngOnInit();

      expect(comp.coursesSharedCollection).toContainEqual(course);
      expect(comp.studentsSharedCollection).toContainEqual(student);
      expect(comp.courseEnrollment).toEqual(courseEnrollment);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICourseEnrollment>>();
      const courseEnrollment = { id: 11487 };
      jest.spyOn(courseEnrollmentFormService, 'getCourseEnrollment').mockReturnValue(courseEnrollment);
      jest.spyOn(courseEnrollmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ courseEnrollment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: courseEnrollment }));
      saveSubject.complete();

      // THEN
      expect(courseEnrollmentFormService.getCourseEnrollment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(courseEnrollmentService.update).toHaveBeenCalledWith(expect.objectContaining(courseEnrollment));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICourseEnrollment>>();
      const courseEnrollment = { id: 11487 };
      jest.spyOn(courseEnrollmentFormService, 'getCourseEnrollment').mockReturnValue({ id: null });
      jest.spyOn(courseEnrollmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ courseEnrollment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: courseEnrollment }));
      saveSubject.complete();

      // THEN
      expect(courseEnrollmentFormService.getCourseEnrollment).toHaveBeenCalled();
      expect(courseEnrollmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICourseEnrollment>>();
      const courseEnrollment = { id: 11487 };
      jest.spyOn(courseEnrollmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ courseEnrollment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(courseEnrollmentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCourse', () => {
      it('should forward to courseService', () => {
        const entity = { id: 2858 };
        const entity2 = { id: 3722 };
        jest.spyOn(courseService, 'compareCourse');
        comp.compareCourse(entity, entity2);
        expect(courseService.compareCourse).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareStudent', () => {
      it('should forward to studentService', () => {
        const entity = { id: 9978 };
        const entity2 = { id: 22718 };
        jest.spyOn(studentService, 'compareStudent');
        comp.compareStudent(entity, entity2);
        expect(studentService.compareStudent).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
