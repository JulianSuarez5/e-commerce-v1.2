'use client';

import * as THREE from 'three';
import { useRef } from 'react';
import { Canvas, useFrame, type CanvasProps } from '@react-three/fiber';
import {
  Float,
  Lightformer,
  Center,
  Environment,
  MeshTransmissionMaterial,
  OrbitControls,
  Icosahedron,
  TorusKnot,
} from '@react-three/drei';
import { easing } from 'maath';

// Configuración de la escena y cámara
const config = {
  meshPhysicalMaterial: {
    thickness: 1.5,
    roughness: 0.1,
    metalness: 0,
    ior: 1.7,
  },
  initialCameraPosition: new THREE.Vector3(0, 0.5, 5),
};

export function HeroOrbitModel(props: Partial<CanvasProps>) {
  return (
    <div className="relative w-full h-[500px] md:h-[600px] rounded-[24px] overflow-hidden bg-gradient-to-br from-[#f0f0f0] to-[#e0e0e0]">
      <Canvas
        shadows
        dpr={[1, 2]}
        camera={{ position: config.initialCameraPosition, fov: 35, near: 0.1, far: 100 }}
        gl={{ antialias: true, alpha: true, toneMapping: THREE.ACESFilmicToneMapping }}
        {...props}
      >
        {/* Iluminación Premium y Entorno */}
        <Environment resolution={256}>
          <group rotation={[-Math.PI / 3, 0, 1]}>
            <Lightformer form="circle" intensity={8} rotation-x={Math.PI / 2} position={[0, 5, -9]} scale={2} />
            <Lightformer form="circle" intensity={2} rotation-y={Math.PI / 2} position={[-5, 1, -1]} scale={2} />
            <Lightformer form="circle" intensity={2} position={[-5, -1, -1]} scale={2} />
            <Lightformer form="rect" intensity={4} rotation-y={Math.PI / 2} position={[10, 1, 0]} scale={[10, 2, 1]} />
          </group>
        </Environment>

        {/* Modelo 3D Principal: Geometría de nudo con material de transmisión */}
        <Float speed={1.5} rotationIntensity={1} floatIntensity={1.2}>
          <Center>
            <TorusKnot args={[1, 0.35, 256, 32]}>
              <MeshTransmissionMaterial {...config.meshPhysicalMaterial} />
            </TorusKnot>
          </Center>
        </Float>

        {/* Elementos 3D flotantes (estilo Apple) */}
        <FloatingShapes />

        {/* Controles de órbita suaves */}
        <OrbitControls
          autoRotate
          autoRotateSpeed={0.3}
          enableZoom={false}
          enablePan={false}
          minPolarAngle={Math.PI / 2.5}
          maxPolarAngle={Math.PI / 2.5}
          minDistance={4}
          maxDistance={6}
        />
      </Canvas>

      {/* Superposición de texto y UI */}
      <div className="absolute inset-0 flex items-center justify-center pointer-events-none">
        <div className="text-center">
          <h1 className="text-4xl md:text-6xl font-bold text-gray-800 tracking-tight">
            Innovación a tu Alcance
          </h1>
          <p className="mt-4 text-lg text-gray-600 max-w-md mx-auto">
            Descubre una nueva era de compras online. Calidad y diseño premium en cada producto.
          </p>
        </div>
      </div>
    </div>
  );
}

// Componente para generar las formas flotantes
function FloatingShapes() {
  return (
    <group position={[0, 0, -1]}>
      {[...Array(20)].map((_, i) => (
        <FloatingShape key={i} />
      ))}
    </group>
  );
}

// Componente para una única forma flotante
function FloatingShape() {
  const ref = useRef<THREE.Group>(null!);

  useFrame((state, delta) => {
    if (!ref.current) return;
    // Movimiento sinusoidal para una animación suave y natural
    const t = state.clock.getElapsedTime() + Math.random() * 10;
    ref.current.position.y = Math.sin(t) * 0.2;
    easing.damp3(ref.current.position, [ref.current.position.x, ref.current.position.y, ref.current.position.z], 0.1, delta);
    easing.dampE(ref.current.rotation, [ref.current.rotation.x, ref.current.rotation.y + 0.1, ref.current.rotation.z], 0.1, delta);
  });

  const randomPosition = () => (Math.random() - 0.5) * 6;

  return (
    <Float speed={2} rotationIntensity={0.5} floatIntensity={1.5}>
      <group ref={ref} position={[randomPosition(), randomPosition(), randomPosition()]}>
        <Icosahedron args={[0.1, 0]} castShadow receiveShadow>
          <MeshTransmissionMaterial {...config.meshPhysicalMaterial} thickness={0.5} />
        </Icosahedron>
      </group>
    </Float>
  );
}
